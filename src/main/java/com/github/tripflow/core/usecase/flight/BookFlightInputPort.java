package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.TripId;

public interface BookFlightInputPort {

    void initializeFlightBookingForCustomer(String taskId);

    void registerSelectedFlightWithTrip(TripId tripId, FlightNumber flightNumber);

    void returnToFlightBookingForCustomer(TripId tripId);
}
