package com.github.tripflow.core.model;

public class Constants {

    /**
     * Process ID for trip booking workflow. Must match process ID in BPMN.
     */
    public static final String TRIPFLOW_PROCESS_ID = "TripFlow";

    /**
     * Roles for TripFlow. Also match "Assignee" attributes for user tasks in BPMN.
     */
    public static final String ROLE_TRIPFLOW_CUSTOMER = "ROLE_TRIPFLOW_CUSTOMER";

    /**
     * Name of the process instance variable identifying a trip associated with corresponding process
     * instance. Will contain process instance key.
     */
    public static final String TRIP_ID_PROCESS_VARIABLE = "tripId";

    /**
     * Name of the variable storing the username of the customer who
     * started the trip.
     */
    public static final String TRIP_STARTED_BY_VARIABLE = "tripStartedBy";

    /**
     * Name of the input variable for each user task. Will contain the action to
     * be executed for the task.
     */
    public static final String ACTION_VARIABLE = "action";

}
