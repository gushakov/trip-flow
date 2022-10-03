package com.github.tripflow.core.model.browse;

import com.github.tripflow.core.model.trip.TripId;
import lombok.Builder;
import lombok.Value;

/**
 * Value object modeling an entry in the list of open trips with
 * some active tasks to be continued by the user. Contains ID of
 * the trip, the next action to be performed, and the status of
 * the trip as a collection of booleans.
 */
@Value
@Builder
public class TripEntry {

    String taskId;
    TripId tripId;
    String taskName;
    String taskAction;
    boolean flightBooked;
    boolean hotelReserved;
}
