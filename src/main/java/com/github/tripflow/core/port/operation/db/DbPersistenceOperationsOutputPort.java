package com.github.tripflow.core.port.operation.db;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;

public interface DbPersistenceOperationsOutputPort {

    Trip saveTrip(Trip trip);

    Trip loadTrip(TripId tripId);


}
