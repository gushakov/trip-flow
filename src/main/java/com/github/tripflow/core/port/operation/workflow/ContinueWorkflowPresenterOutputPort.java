package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

public interface ContinueWorkflowPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentNextActiveTaskForUser(TripTask tripTask);

    void presentNoActiveTasksFoundForUserResult(TripId tripId);
}
