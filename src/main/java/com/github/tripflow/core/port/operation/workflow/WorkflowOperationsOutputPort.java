package com.github.tripflow.core.port.operation.workflow;

public interface WorkflowOperationsOutputPort {

    Long startNewTripBookingProcess();

    void cancelTripBookingProcess(Long pik);
}
