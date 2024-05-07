package com.github.tripflow.core.port.db;

import com.github.tripflow.core.GenericTripFlowError;

public class TripFlowDbPersistenceError extends GenericTripFlowError {
    public TripFlowDbPersistenceError(String message) {
        super(message);
    }

    public TripFlowDbPersistenceError(String message, Throwable cause) {
        super(message, cause);
    }
}
