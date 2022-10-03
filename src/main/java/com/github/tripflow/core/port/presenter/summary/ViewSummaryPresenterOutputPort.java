package com.github.tripflow.core.port.presenter.summary;

import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;
import com.github.tripflow.core.model.summary.TripSummary;

public interface ViewSummaryPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentTripSummary(TripSummary tripSummary);
}
