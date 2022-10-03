package com.github.tripflow.core.usecase.creditcheck;

import com.github.tripflow.core.model.trip.TripId;

public interface CheckCreditInputPort {

    void checkCreditLimit(TripId tripId);

}
