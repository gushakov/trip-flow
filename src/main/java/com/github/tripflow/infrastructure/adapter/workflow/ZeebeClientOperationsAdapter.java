package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.port.operation.workflow.WorkflowClientOperationError;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import io.camunda.zeebe.client.api.command.ClientException;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZeebeClientOperationsAdapter implements WorkflowOperationsOutputPort {

    private final ZeebeClientLifecycle zeebeClient;


    @Override
    public Long startNewTripBookingProcess() {
        ProcessInstanceEvent start;
        try {
            start = zeebeClient
                    .newCreateInstanceCommand()
                    .bpmnProcessId(Constants.TRIPFLOW_PROCESS_ID)
                    .latestVersion()
                    .send()
                    .join();
        } catch (ClientException e) {
            throw new WorkflowClientOperationError(e.getMessage(), e);
        }

        return start.getProcessDefinitionKey();
    }

    @Override
    public void cancelTripBookingProcess(Long pik) {

        try {
            zeebeClient.newCancelInstanceCommand(pik)
                    .send()
                    .join();
        } catch (ClientException e) {
            throw new WorkflowClientOperationError(e.getMessage(), e);
        }
    }
}
