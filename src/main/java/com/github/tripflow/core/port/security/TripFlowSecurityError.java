package com.github.tripflow.core.port.security;

import com.github.tripflow.core.GenericTripFlowError;

public class TripFlowSecurityError extends GenericTripFlowError {

    public TripFlowSecurityError(String message) {
        super(message);
    }

    public TripFlowSecurityError(Exception e) {
        super(e.getMessage(), e);
    }
}
