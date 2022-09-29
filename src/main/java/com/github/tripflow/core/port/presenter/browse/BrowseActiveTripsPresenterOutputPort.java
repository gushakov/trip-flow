package com.github.tripflow.core.port.presenter.browse;

import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BrowseActiveTripsPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentListOfTripsByActiveTasksAssignedToUser(List<TripTask> tasks);
}
