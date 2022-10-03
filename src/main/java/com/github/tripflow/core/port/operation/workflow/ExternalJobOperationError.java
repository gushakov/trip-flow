package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.GenericTripFlowError;

public class ExternalJobOperationError extends GenericTripFlowError {
    public ExternalJobOperationError(String message, Throwable cause) {
        super(message, cause);
    }
}
