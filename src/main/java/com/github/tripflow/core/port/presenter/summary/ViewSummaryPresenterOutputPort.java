package com.github.tripflow.core.port.presenter.summary;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

public interface ViewSummaryPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentTripSummary(Trip trip);
}
