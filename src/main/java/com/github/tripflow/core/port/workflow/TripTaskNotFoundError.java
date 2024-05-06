package com.github.tripflow.core.port.workflow;

import com.github.tripflow.core.GenericTripFlowError;

/**
 * Error thrown when no {@code TripTask} could be found in the database.
 * Note there is a {@code RetryTemplate} configured to retry on this error.
 */
public class TripTaskNotFoundError extends GenericTripFlowError {
}
