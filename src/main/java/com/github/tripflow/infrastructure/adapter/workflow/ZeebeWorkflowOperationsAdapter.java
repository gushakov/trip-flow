package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.port.operation.workflow.WorkflowClientOperationError;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import io.camunda.zeebe.client.api.command.ClientException;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.SetVariablesResponse;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZeebeWorkflowOperationsAdapter implements WorkflowOperationsOutputPort {

    private final ZeebeClientLifecycle zeebeClientLifecycle;

    private final JobClient jobClient;
    @Override
    public Long startNewTripBookingProcess(String tripStartedBy) {
        ProcessInstanceEvent start;
        try {
            start = zeebeClientLifecycle
                    .newCreateInstanceCommand()
                    .bpmnProcessId(Constants.TRIPFLOW_PROCESS_ID)
                    .latestVersion()
                    .send()
                    .join();
            long pik = start.getProcessInstanceKey();

            log.debug("[Zeebe Client] Started new instance of process: {} with process instance key: {}",
                    Constants.TRIPFLOW_PROCESS_ID, pik);

            // store process instance key (trip ID) and the username of the customer who started
            // the trip as the flow variables
            SetVariablesResponse variableStatus = zeebeClientLifecycle.newSetVariablesCommand(pik)
                    .variables(Map.of(Constants.TRIP_ID_PROCESS_VARIABLE, pik,
                            Constants.TRIP_STARTED_BY_VARIABLE, tripStartedBy))
                    .send()
                    .join();

            log.debug("[Zeebe Client] Set trip ID variable for the new process: {}, variable key: {}",
                    pik, variableStatus.getKey());

        } catch (ClientException e) {
            throw new WorkflowClientOperationError(e.getMessage(), e);
        }

        return start.getProcessInstanceKey();
    }

    @Override
    public void cancelTripBookingProcess(Long pik) {
        try {
            zeebeClientLifecycle.newCancelInstanceCommand(pik)
                    .send()
                    .join();
            log.debug("[Zeebe Client] Canceled process instance {}", pik);
        } catch (ClientException e) {
            throw new WorkflowClientOperationError(e.getMessage(), e);
        }
    }
}
