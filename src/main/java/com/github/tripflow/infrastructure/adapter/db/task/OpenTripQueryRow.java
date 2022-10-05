package com.github.tripflow.infrastructure.adapter.db.task;

import lombok.Data;

@Data
public class OpenTripQueryRow {

    public static String SQL = "select tt.trip_id as \"trip_id\", tt.task_id as \"task_id\", tt.\"name\" as \"task_name\",  tt.\"action\" as \"task_action\", tp.flight_booked as \"flight_booked\", tp.hotel_reserved as \"hotel_reserved\" " +
            "from trip_task tt join trip tp on tt.trip_id = tp.trip_id  " +
            "where tt.candidate_groups = :candidate_groups " +
            "and tt.trip_started_by = :trip_started_by;";

    Long tripId;
    Long taskId;
    String taskName;
    String taskAction;
    Boolean flightBooked;
    Boolean hotelReserved;

}
