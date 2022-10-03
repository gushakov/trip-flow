package com.github.tripflow.core.port.presenter.summary;

import com.github.tripflow.core.model.trip.TripSummary;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

public interface ViewSummaryPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentTripSummary(TripSummary tripSummary);
}
