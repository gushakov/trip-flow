package com.github.tripflow.core.port.presenter.browse;

import com.github.tripflow.core.model.browse.TripEntry;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BrowseActiveTripsPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentActiveTasksForTripsStartedByUser(List<TripEntry> tripEntries);
}
