package com.github.tripflow.core.port.operation.db;

import com.github.tripflow.core.model.trip.Trip;

public interface DbPersistenceOperationsOutputPort {

    Trip save(Trip trip);

}
