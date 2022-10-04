package com.github.tripflow.core.model.task;

import com.github.tripflow.core.model.trip.TripId;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.github.tripflow.core.model.Validator.notNull;

/**
 * Aggregate which represents a user or a service task in TripFlow process.
 * Allows access to the variables of the task which are modeled
 * in the BPMN. Note, the task variables may not be available for
 * a lapse of time between state transitions of the workflow.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TripTask {

    /**
     * ID of the task in the workflow, corresponds to the key of the corresponding job.
     */
    @EqualsAndHashCode.Include
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
     * User-friendly task name. Corresponds to the BPMN "name" of a user task,
     * or to the BPMN "type" of a service task.
     */
    String name;

    /**
     * Action to be executed for the task. Could be {@code null} for "service tasks".
     */
    String action;

    public TripTask(String taskId, TripId tripId, String tripStartedBy, String name, String action) {
        this.taskId = notNull(taskId);
        this.tripId = notNull(tripId);
        this.tripStartedBy = notNull(tripStartedBy);
        this.name = notNull(name);
        this.action = action;
    }
}
