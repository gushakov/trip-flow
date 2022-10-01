package com.github.tripflow.core.model.trip;

import lombok.Builder;
import lombok.Value;

/**
 * Value object modeling all information relevant to an instance of a user task in
 * a particular TripFlow process instance. Encapsulates workflow variables associated
 * with the task.
 */
@Value
@Builder
public class TripTask {

    /**
     * ID of the task (job) in the workflow.
     */
    String taskId;

    /**
     * TripFlow process instance key, also the ID of the
     * corresponding {@code Trip} instance.
     */
    TripId tripId;

    /**
     * Customer who started the trip.
     */
    String tripStartedBy;

    /**
     * User-friendly task name.
     */
    String name;

    /**
     * Action to be executed for the task.
     */
    String action;

}
