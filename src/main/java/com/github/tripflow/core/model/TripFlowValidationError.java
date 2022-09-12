package com.github.tripflow.core.model;

import com.github.tripflow.core.GenericTripFlowError;

public class TripFlowValidationError extends GenericTripFlowError {
    public TripFlowValidationError(String message) {
        super(message);
    }
}
