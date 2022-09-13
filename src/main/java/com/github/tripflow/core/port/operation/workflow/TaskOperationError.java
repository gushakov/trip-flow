package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.GenericTripFlowError;

public class TaskOperationError extends GenericTripFlowError {
    public TaskOperationError(String message, Throwable cause) {
        super(message, cause);
    }
}
