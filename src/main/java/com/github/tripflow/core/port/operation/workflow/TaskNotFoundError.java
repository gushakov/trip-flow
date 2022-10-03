package com.github.tripflow.core.port.operation.workflow;

/**
 * Special subtype of {@linkplain TaskOperationError} which will trigger
 * the retry retrieval of the task with required variables from
 * the workflow.
 *
 * @see com.github.tripflow.infrastructure.config.AppConfig
 */
public class TaskNotFoundError extends TaskOperationError {
}
