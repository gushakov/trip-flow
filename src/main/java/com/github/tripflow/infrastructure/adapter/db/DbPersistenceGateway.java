package com.github.tripflow.infrastructure.adapter.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.db.TripFlowDbPersistenceError;
import com.github.tripflow.infrastructure.adapter.db.map.TripDbMapper;
import com.github.tripflow.infrastructure.utils.StreamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbPersistenceGateway implements DbPersistenceOperationsOutputPort {

    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    private final TripDbMapper dbMapper;

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
            return dbMapper.convert(jdbcAggregateTemplate.findById(tripId.getId(), TripEntity.class));
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot load trip with ID: %s"
                    .formatted(tripId), e);
        }
    }

    @Override
    public List<Flight> loadAllFlights() {
        try {
            Iterable<FlightEntity> allFlightsEntities = jdbcAggregateTemplate.findAll(FlightEntity.class);
            return StreamUtils.toStream(allFlightsEntities.iterator())
                    .map(dbMapper::convert)
                    .toList();
        } catch (Exception e) {
            throw new TripFlowDbPersistenceError("Cannot load all flights", e);
        }
    }
}
