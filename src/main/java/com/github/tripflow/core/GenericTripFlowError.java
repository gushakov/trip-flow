package com.github.tripflow.core;

/**
 * Topmost runtime error for TripFlow.
 */
public class GenericTripFlowError extends RuntimeException {

    public GenericTripFlowError(String message) {
        super(message);
    }
}
