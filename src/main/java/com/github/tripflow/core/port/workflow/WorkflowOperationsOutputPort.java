package com.github.tripflow.core.port.workflow;

import com.github.tripflow.core.TripFlowBpmnError;

public interface WorkflowOperationsOutputPort {

    Long startNewTripBookingProcess(String tripStartedBy);

    void cancelTripBookingProcess(Long pik);

    void throwBpmnError(Long taskId, TripFlowBpmnError error);

    void completeCreditCheck(Long taskId, boolean sufficientCredit);

    void completeTask(Long taskId);
}
