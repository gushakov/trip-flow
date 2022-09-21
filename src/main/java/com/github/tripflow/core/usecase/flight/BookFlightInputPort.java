package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.model.trip.TripId;

public interface BookFlightInputPort {

    void initializeFlightBookingForCustomer(TripId tripId);

}
