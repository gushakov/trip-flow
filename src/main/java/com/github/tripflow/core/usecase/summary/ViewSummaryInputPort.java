package com.github.tripflow.core.usecase.summary;

public interface ViewSummaryInputPort {

    void viewTripSummary(Long taskId);

    void proceedWithPayment(Long taskId);

}
