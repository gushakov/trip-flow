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
     * Equals to the process instance key.
     */
    TripId tripId;

    String taskId;

    /**
     * User-friendly task name.
     */
    String name;

    /**
     * Action to be executed for the task.
     */
    String action;

}
