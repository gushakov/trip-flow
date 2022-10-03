package com.github.tripflow.core.usecase.confirmation;

import com.github.tripflow.core.model.trip.TripId;

public interface ConfirmTripInputPort {
    void confirmTrip(TripId tripId);

    void cancelTrip(TripId tripId);
}
