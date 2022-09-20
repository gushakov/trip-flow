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
     * Name of the variable associated with each process instance identifying
     * each trip.
     */
    public static final String TRIP_ID_PROCESS_VARIABLE = "tripId";

}
