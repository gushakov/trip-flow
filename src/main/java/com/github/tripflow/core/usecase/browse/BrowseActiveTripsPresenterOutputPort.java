package com.github.tripflow.core.usecase.browse;

import com.github.tripflow.core.model.trip.TripEntry;
import com.github.tripflow.core.port.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BrowseActiveTripsPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentActiveTasksForTripsStartedByUser(List<TripEntry> tripEntries);
}
