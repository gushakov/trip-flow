package com.github.tripflow.core.port.operation.workflow;

public interface WorkflowOperationsOutputPort {

    Long startNewTripBookingProcess(String tripStartedBy);

    void cancelTripBookingProcess(Long pik);
}
