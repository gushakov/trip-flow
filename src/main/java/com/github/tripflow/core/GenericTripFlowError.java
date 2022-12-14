package com.github.tripflow.core;

/**
 * Topmost runtime error for TripFlow.
 */
public class GenericTripFlowError extends RuntimeException {
    public GenericTripFlowError() {
    }

    public GenericTripFlowError(String message) {
        super(message);
    }

    public GenericTripFlowError(String message, Throwable cause) {
        super(message, cause);
    }
}
