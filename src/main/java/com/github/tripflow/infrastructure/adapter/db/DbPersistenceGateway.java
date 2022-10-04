package com.github.tripflow.infrastructure.adapter.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.db.TripFlowDbPersistenceError;
import com.github.tripflow.core.port.operation.workflow.TripTaskNotFoundError;
import com.github.tripflow.infrastructure.adapter.db.flight.FlightEntityRepository;
import com.github.tripflow.infrastructure.adapter.db.hotel.HotelEntityRepository;
import com.github.tripflow.infrastructure.adapter.db.map.TripFlowDbMapper;
import com.github.tripflow.infrastructure.adapter.db.task.TripTaskEntityRepository;
import com.github.tripflow.infrastructure.adapter.db.trip.TripEntity;
import com.github.tripflow.infrastructure.adapter.db.trip.TripEntityRepository;
import com.github.tripflow.infrastructure.config.TripFlowProperties;
import com.github.tripflow.infrastructure.utils.StreamUtils;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbPersistenceGateway implements DbPersistenceOperationsOutputPort {

    private final TripFlowProperties tripFlowProps;

    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    private final TripFlowDbMapper dbMapper;

    private final RetryTemplate retryTemplate;

    private final TripTaskEntityRepository taskEntityRepo;

    private final TripEntityRepository tripEntityRepo;
    private final HotelEntityRepository hotelEntityRepo;
    private final FlightEntityRepository flightEntityRepo;

    public DbPersistenceGateway(TripFlowProperties tripFlowProps,
                                JdbcAggregateTemplate jdbcAggregateTemplate,
                                TripFlowDbMapper dbMapper,
                                TripTaskEntityRepository taskEntityRepo,
                                TripEntityRepository tripEntityRepo,
                                HotelEntityRepository hotelEntityRepo,
                                FlightEntityRepository flightEntityRepo) {
        this.tripFlowProps = tripFlowProps;
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
        this.dbMapper = dbMapper;
        this.retryTemplate = initRetryTemplate();
        this.taskEntityRepo = taskEntityRepo;
        this.tripEntityRepo = tripEntityRepo;
        this.hotelEntityRepo = hotelEntityRepo;
        this.flightEntityRepo = flightEntityRepo;
    }

    private RetryTemplate initRetryTemplate() {
        return new RetryTemplateBuilder()
                .fixedBackoff(500L)
                .retryOn(TripTaskNotFoundError.class)
                .maxAttempts(tripFlowProps.getTaskLookUpMaxRetries())
                .build();
    }

    @Override
    public Trip saveNewTrip(Trip trip) {
        try {
            TripEntity saved = jdbcAggregateTemplate.insert(dbMapper.convert(trip));
            return dbMapper.convert(saved);
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot save Trip: %s"
                    .formatted(trip.getTripId()), e);
        }
    }

    @Override
    public Trip updateTrip(Trip trip) {
        try {
            TripEntity updated = jdbcAggregateTemplate.update(dbMapper.convert(trip));
            return dbMapper.convert(updated);
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot update Trip: %s"
                    .formatted(trip.getTripId()), e);
        }
    }

    @Override
    public Trip loadTrip(TripId tripId) {
        try {
            return dbMapper.convert(tripEntityRepo.findById(tripId.getId())
                    .orElseThrow(IllegalStateException::new));
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot load trip with ID: %s"
                    .formatted(tripId), e);
        }
    }

    @Override
    public List<Flight> loadAllFlights() {
        try {
            return StreamUtils.toStream(flightEntityRepo.findAll().iterator())
                    .map(dbMapper::convert)
                    .toList();
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot load all flights", e);
        }
    }

    @Override
    public List<Hotel> hotelsInCity(String city) {

        try {
            return hotelEntityRepo.findAllByCity(city).stream()
                    .map(dbMapper::convert)
                    .toList();
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot find hotels by city: %s"
                    .formatted(city), e);
        }
    }

    @Override
    public Flight loadFlight(FlightNumber flightNumber) {
        try {
            return dbMapper.convert(flightEntityRepo.findById(flightNumber.getNumber())
                    .orElseThrow(IllegalStateException::new));
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot load flight with number: %s"
                    .formatted(flightNumber), e);
        }
    }

    @Override
    public Hotel loadHotel(HotelId hotelId) {
        try {
            return dbMapper.convert(hotelEntityRepo.findById(hotelId.getId())
                    .orElseThrow(IllegalStateException::new));
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot load hotel with ID: %s"
                    .formatted(hotelId), e);
        }
    }

    @Override
    public void saveTripTask(TripTask tripTask) {
        jdbcAggregateTemplate.save(tripTask);
    }

    @Override
    public TripTask loadTripTask(Long taskId) {
        try {
            return dbMapper.convert(taskEntityRepo.findById(taskId).orElseThrow());
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot find trip task with ID: %s"
                    .formatted(taskId), e);
        }
    }

    @Override
    public List<TripTask> findAnyTasksForTripAndUser(TripId tripId,
                                                     String candidateGroups,
                                                     String tripStartedBy) {
        try {
            return retryTemplate.execute(retryContext -> {

                List<TripTask> tasks = taskEntityRepo.findAllByTripIdAndCandidateGroupsAndTripStartedBy(tripId.getId(),
                                candidateGroups, tripStartedBy)
                        .stream()
                        .map(dbMapper::convert)
                        .toList();

                if (tasks.isEmpty()) {
                    // retry
                    throw new TripTaskNotFoundError();
                }

                return tasks;
            });
        } catch (TripTaskNotFoundError e) {
            // will happen if retry limit has been exceeded
            return List.of();
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError(("Error when searching for all tasks of the trip with ID: %s," +
                    " , started by %s, and assigned to the candidate group: %s")
                    .formatted(tripId, tripStartedBy, candidateGroups), e);
        }
    }

    @Override
    public List<TripTask> findAnyTasksForUser(String candidateGroups,
                                              String tripStartedBy) {
        try {
            return taskEntityRepo.findAllByCandidateGroupsAndTripStartedBy(
                            candidateGroups, tripStartedBy)
                    .stream()
                    .map(dbMapper::convert)
                    .toList();
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError(("Error when searching for all tasks, started by %s," +
                    " and assigned to the candidate group: %s")
                    .formatted(tripStartedBy, candidateGroups), e);
        }
    }

}
