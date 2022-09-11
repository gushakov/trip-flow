package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.GenericTripFlowError;

public class WorkflowClientOperationError extends GenericTripFlowError {
    public WorkflowClientOperationError(String message) {
        super(message);
    }

    public WorkflowClientOperationError(String message, Throwable cause) {
        super(message, cause);
    }
}
