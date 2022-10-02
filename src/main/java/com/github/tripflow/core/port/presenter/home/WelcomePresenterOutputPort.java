package com.github.tripflow.core.port.presenter.home;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.workflow.WorkflowClientOperationError;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

public interface WelcomePresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentWelcomeView(String userName, boolean isCustomer);

    void presentResultOfStartingNewTripBookingWithoutNextActiveTask(TripId tripId);

    void presentResultOfStartingNewTripBookingWithNextActiveTask(TripTask tripTask);
}
