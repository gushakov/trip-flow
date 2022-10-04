package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.TripId;

public interface BookFlightInputPort {

    void proposeFlightsForSelectionByCustomer(Long taskId);

    void registerSelectedFlightWithTrip(String taskId, TripId tripId, FlightNumber flightNumber);

    void confirmFlightBooking(Long taskId);
}
