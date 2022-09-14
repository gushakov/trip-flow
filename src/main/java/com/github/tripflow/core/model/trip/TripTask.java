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

    TripId tripId;

    String taskId;

    String name;

}
