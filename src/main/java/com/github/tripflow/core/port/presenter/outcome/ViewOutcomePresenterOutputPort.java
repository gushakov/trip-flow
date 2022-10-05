package com.github.tripflow.core.port.presenter.outcome;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

public interface ViewOutcomePresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentSuccessfulTripConfirmation(Long taskId, TripId tripId, String city);

    void presentRefusedTripBooking(Long taskId, TripId tripId, String city);

    void presentResultOfSuccessfullyFinishingProcess(TripId tripId);

}
