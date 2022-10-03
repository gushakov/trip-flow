package com.github.tripflow.core.model.summary;

import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.TripId;
import lombok.Builder;
import lombok.Value;

/**
 * Value object with the summary of the trip to be confirmed.
 */
@Value
@Builder
public class TripSummary {

    String taskId;
    TripId tripId;

    FlightNumber flightNumber;

    String flightOrigin;
    String flightDestination;
    int flightPrice;

    String hotelName;
    int hotelPrice;

    int totalPrice;
}
