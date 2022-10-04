package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.TripFlowBpmnError;
import com.github.tripflow.core.model.task.TripTask;

public interface ExternalJobOperationsOutputPort {

    TripTask activeTripTask();

    void throwError(TripFlowBpmnError error);

    void completeCreditCheck(boolean sufficientCredit);

    void completeTask();

}
