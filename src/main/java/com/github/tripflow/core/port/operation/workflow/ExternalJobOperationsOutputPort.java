package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.TripFlowBpmnError;

public interface ExternalJobOperationsOutputPort {

    void throwError(TripFlowBpmnError error);

    void completeCreditCheck(boolean sufficientCredit);

}
