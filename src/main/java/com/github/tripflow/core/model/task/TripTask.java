package com.github.tripflow.core.model.task;

import com.github.tripflow.core.model.trip.TripId;
import lombok.AccessLevel;
import lombok.Builder;
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
    Long taskId;

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
     * Action to be executed for the user task. Will correspond to the BPMN "type" for
     * a service task.
     */
    String action;

    /**
     * Candidate groups (roles) assigned to the task.
     */
    String candidateGroups;

    Integer version;

    @Builder
    public TripTask(Long taskId, TripId tripId, String tripStartedBy, String name, String action, String candidateGroups, Integer version) {
        this.taskId = notNull(taskId);
        this.tripId = notNull(tripId);
        this.tripStartedBy = notNull(tripStartedBy);
        this.name = notNull(name);
        this.action = notNull(action);
        this.candidateGroups = notNull(candidateGroups);

        // this can be null for tasks which we not yet persisted
        this.version = version;
    }
}
