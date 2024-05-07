package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.TripFlowBpmnError;
import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.port.workflow.WorkflowClientOperationError;
import com.github.tripflow.core.port.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.infrastructure.adapter.db.DbPersistenceGateway;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.command.ClientException;
import io.camunda.zeebe.client.api.command.CompleteJobCommandStep1;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/*
    References:
    ----------

    1. Starting a new process instance: https://github.com/camunda-community-hub/spring-zeebe/blob/main/example/src/main/java/io/camunda/zeebe/spring/example/PeriodicProcessStarter.java
 */

/**
 * Secondary adapter which uses {@link ZeebeClient} and {@link JobClient} to communicate
 * with Zeebe.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ZeebeClientOperationsAdapter implements WorkflowOperationsOutputPort {

    private final ZeebeClient zeebeClient;

    private final JobClient jobClient;

    private final DbPersistenceGateway dbGateway;

    @Override
    public Long startNewTripBookingProcess(String tripStartedBy) {
        ProcessInstanceEvent start;
        try {

            // start new workflow process, store the username of the current user in "tripStartedBy"
            // process instance variable

            start = zeebeClient
                    .newCreateInstanceCommand()
                    .bpmnProcessId(Constants.TRIPFLOW_PROCESS_ID)
                    .latestVersion()
                    .variables(Map.of(Constants.TRIP_STARTED_BY_VARIABLE, tripStartedBy))
                    .send()
                    .join();
            long pik = start.getProcessInstanceKey();

            log.debug("[Zeebe Client] Started new instance of process: {}, by user: {}, with process instance key: {}",
                    Constants.TRIPFLOW_PROCESS_ID, tripStartedBy, pik);

        } catch (ClientException e) {
            throw new WorkflowClientOperationError(e.getMessage(), e);
        }

        return start.getProcessInstanceKey();
    }

    @Override
    public void cancelTripBookingProcess(Long pik) {
        try {
            zeebeClient.newCancelInstanceCommand(pik)
                    .send()
                    .join();
            log.debug("[Zeebe Client] Canceled process instance {}", pik);
        } catch (ClientException e) {
            throw new WorkflowClientOperationError(e.getMessage(), e);
        }
    }

    @Override
    public void throwBpmnError(Long taskId, TripFlowBpmnError error) {
        // log error for debugging
        log.error(error.getMessage(), error);

        try {
            jobClient.newThrowErrorCommand(taskId)
                    .errorCode(error.getBpmnCode())
                    .errorMessage(error.getMessage())
                    .send();
        } catch (Exception e) {
            throw new WorkflowClientOperationError("Cannot complete service task, job key: %s"
                    .formatted(taskId), e);
        }
    }

    @Override
    public void completeCreditCheck(Long taskId, boolean sufficientCredit) {
        doCompleteTask(taskId, Map.of(Constants.SUFFICIENT_CREDIT_VARIABLE, sufficientCredit));
    }

    @Override
    public void completeTask(Long taskId) {
        doCompleteTask(taskId, null);
    }

    protected void doCompleteTask(Long taskId, Map<String, Object> variables) {
        try {
            CompleteJobCommandStep1 commandStep = jobClient.newCompleteCommand(taskId);
            if (variables != null && !variables.isEmpty()) {
                commandStep.variables(variables);
            }
            commandStep.send();

            // remove task from the database
            dbGateway.removeTripTask(taskId);
        } catch (Exception e) {
            // log error
            log.error(e.getMessage(), e);
            throw new WorkflowClientOperationError("Cannot complete task with ID: %s, and variables: %s"
                    .formatted(taskId, variables), e);
        }
    }
}
