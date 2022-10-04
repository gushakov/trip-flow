package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.TripFlowBpmnError;

public interface WorkflowOperationsOutputPort {

    Long startNewTripBookingProcess(String tripStartedBy);

    void cancelTripBookingProcess(Long pik);

    void throwBpmnError(Long jobKey, TripFlowBpmnError error);


    void completeCreditCheck(Long jobKey, boolean sufficientCredit);

    void completeTask(Long jobKey);
}
