package com.github.tripflow.core.port.presenter.summary;

import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripSummary;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

public interface ViewSummaryPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentTripSummary(TripSummary tripSummary);

    void presentResultOfProceedingWithPaymentWithoutNextActiveTask(TripId tripId);

    void presentResultOfProceedingWithPaymentWithNextActiveTask(TripTask tripTask);
}
