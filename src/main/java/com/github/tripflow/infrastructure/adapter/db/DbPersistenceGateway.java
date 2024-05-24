package com.github.tripflow.infrastructure.adapter.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripEntry;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.db.TripFlowDbPersistenceError;
import com.github.tripflow.core.port.workflow.TripTaskNotFoundError;
import com.github.tripflow.infrastructure.adapter.db.flight.FlightEntityRepository;
import com.github.tripflow.infrastructure.adapter.db.hotel.HotelEntityRepository;
import com.github.tripflow.infrastructure.adapter.db.map.TripFlowDbMapper;
import com.github.tripflow.infrastructure.adapter.db.task.OpenTripQueryRow;
import com.github.tripflow.infrastructure.adapter.db.task.TripTaskEntityRepository;
import com.github.tripflow.infrastructure.adapter.db.trip.TripEntityRepository;
import com.github.tripflow.infrastructure.config.TripFlowProperties;
import com.github.tripflow.infrastructure.utils.StreamUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

/*
    References:
    ----------

    1.  Rollback active transaction: https://stackoverflow.com/a/23502214
    2.  Spring, TransactionTemplate: source code and JavaDoc
 */

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class DbPersistenceGateway implements DbPersistenceOperationsOutputPort {

    TripFlowProperties tripFlowProps;

    JdbcAggregateTemplate jdbcAggregateTemplate;

    NamedParameterJdbcOperations jdbcQueryOps;

    TransactionTemplate transactionTemplate;

    TripFlowDbMapper dbMapper;

    RetryTemplate retryTemplate;

    TripTaskEntityRepository taskEntityRepo;

    TripEntityRepository tripEntityRepo;
    HotelEntityRepository hotelEntityRepo;
    FlightEntityRepository flightEntityRepo;

    public DbPersistenceGateway(TripFlowProperties tripFlowProps,
                                JdbcAggregateTemplate jdbcAggregateTemplate,
                                NamedParameterJdbcOperations jdbcQueryOps, TransactionTemplate transactionTemplate, TripFlowDbMapper dbMapper,
                                TripTaskEntityRepository taskEntityRepo,
                                TripEntityRepository tripEntityRepo,
                                HotelEntityRepository hotelEntityRepo,
                                FlightEntityRepository flightEntityRepo) {
        this.tripFlowProps = tripFlowProps;
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
        this.jdbcQueryOps = jdbcQueryOps;
        this.transactionTemplate = transactionTemplate;
        this.dbMapper = dbMapper;
        this.retryTemplate = initRetryTemplate();
        this.taskEntityRepo = taskEntityRepo;
        this.tripEntityRepo = tripEntityRepo;
        this.hotelEntityRepo = hotelEntityRepo;
        this.flightEntityRepo = flightEntityRepo;
    }

    private RetryTemplate initRetryTemplate() {
        return new RetryTemplateBuilder()
                .fixedBackoff(tripFlowProps.getTaskLookUpBackoffIntervalMillis())
                .retryOn(TripTaskNotFoundError.class)
                .maxAttempts(tripFlowProps.getTaskLookUpMaxRetries())
                .build();
    }

    @Override
    public void doInTransaction(Runnable runnable) {
        try {
            transactionTemplate.executeWithoutResult(status -> runnable.run());
        } catch (TransactionException | Error e) {
            throw new TripFlowDbPersistenceError("Error while executing transaction", e);
        }
    }

    @Transactional
    @Override
    public void saveNewTrip(Trip trip) {
        try {
            jdbcAggregateTemplate.insert(dbMapper.convert(trip));
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot save Trip: %s"
                    .formatted(trip.getTripId()), e);
        }
    }

    @Transactional
    @Override
    public void updateTrip(Trip trip) {
        try {
            jdbcAggregateTemplate.update(dbMapper.convert(trip));
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot update Trip: %s"
                    .formatted(trip.getTripId()), e);
        }
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional
    @Override
    public void saveTripTaskIfNeeded(TripTask tripTask) {
        Long taskId = tripTask.getTaskId();
        try {
            // do not try to save again when Zeebe's worker tries to
            // handle the user task again
            if (!taskEntityRepo.existsById(taskId)) {
                taskEntityRepo.save(dbMapper.convert(tripTask));
            }
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot save trip task with ID: %s"
                    .formatted(taskId), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public TripTask loadTripTask(Long taskId) {
        try {
            return dbMapper.convert(taskEntityRepo.findById(taskId).orElseThrow());
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot find trip task with ID: %s"
                    .formatted(taskId), e);
        }
    }

    @Transactional
    @Override
    public void removeTripTask(Long taskId) {
        try {
            taskEntityRepo.deleteById(taskId);
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot delete trip task with ID: %s"
                    .formatted(taskId), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public boolean tripTaskExists(Long taskId) {
        return taskEntityRepo.existsById(taskId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TripTask> findTasksForTripAndUserWithRetry(TripId tripId,
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
                    " trip started by %s, and assigned to the candidate group: %s")
                    .formatted(tripId, tripStartedBy, candidateGroups), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TripTask> findTasksForUser(String candidateGroups,
                                           String tripStartedBy) {
        try {
            return taskEntityRepo.findAllByCandidateGroupsAndTripStartedBy(
                            candidateGroups, tripStartedBy)
                    .stream()
                    .map(dbMapper::convert)
                    .toList();
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError(("Error when searching for all tasks, trip started by %s," +
                    " and assigned to the candidate group: %s")
                    .formatted(tripStartedBy, candidateGroups), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TripEntry> findAllOpenTripsForUser(String candidateGroups, String tripStartedBy) {
        try {
            return jdbcQueryOps.queryForStream(OpenTripQueryRow.SQL,
                            Map.of("candidate_groups", candidateGroups,
                                    "trip_started_by", tripStartedBy),
                            new BeanPropertyRowMapper<>(OpenTripQueryRow.class))
                    .map(dbMapper::convert)
                    .toList();
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Error when executing query for open trips, candidate groups: %s, trip started by: %s"
                    .formatted(candidateGroups, tripStartedBy), e);

        }
    }

}
