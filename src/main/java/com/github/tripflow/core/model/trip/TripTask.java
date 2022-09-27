package com.github.tripflow.core.model.trip;

import lombok.Builder;
import lombok.Value;

import java.util.List;

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
     * TripFlow process instance key.
     */
    TripId tripId;

    /**
     * User-friendly task name.
     */
    String name;

    /**
     * Action to be executed for the task.
     */
    String action;

    /**
     * Task is active if corresponding workflow task is "CREATED".
     */
    boolean active;

    /**
     * Corresponds to the "Assignee" of the workflow task, "Assignment" section.
     */
    String assignee;

    /**
     * Corresponds to the "Candidate groups" of the workflow task, "Assignment" section.
     */
    List<String> candidateGroups;

    /**
     * Will be {@code true} if a flight booking has been complete.
     */
    boolean flightBooked;

}
