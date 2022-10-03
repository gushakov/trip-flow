package com.github.tripflow.core.model;

import com.github.tripflow.core.GenericTripFlowError;

/**
 * This error will be thrown if some aggregate invariant does not
 * hold or if we are in an inconsistent workflow state: meaning
 * that, a state of {@code Trip} aggregate dos not correspond
 * to the logical state dictated by state of the workflow instance
 * (during a user task) represented by {@code TripTask}.
 */
public class TripFlowValidationError extends GenericTripFlowError {
    public TripFlowValidationError(String message) {
        super(message);
    }
}
