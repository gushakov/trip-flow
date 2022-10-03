package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.GenericTripFlowError;

/**
 * Error when performing some operation using TaskList client: getting
 * information about a task and related variables from the workflow
 * engine.
 */
public class TaskOperationError extends GenericTripFlowError {
    public TaskOperationError() {
    }

    public TaskOperationError(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskOperationError(String message) {
        super(message);
    }
}
