package com.github.tripflow.core.usecase.flight;

public interface BookFlightInputPort {

    void initializeFlightBookingForCustomer(String taskId);

    void registerSelectedFlightWithTrip(Long tripId, String flightNumber);

    void returnToFlightBookingForCustomer(Long tripId);
}
