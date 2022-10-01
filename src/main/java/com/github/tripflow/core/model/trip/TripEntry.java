package com.github.tripflow.core.model.trip;

import lombok.Builder;
import lombok.Value;

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
