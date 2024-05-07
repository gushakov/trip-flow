package com.github.tripflow.core.usecase.home;

import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.ErrorHandlingPresenterOutputPort;

public interface WelcomePresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentWelcomeView(String userName, boolean isCustomer);

    void presentResultOfStartingNewTripBookingWithoutNextActiveTask(TripId tripId);

    void presentResultOfStartingNewTripBookingWithNextActiveTask(TripTask tripTask);
}
