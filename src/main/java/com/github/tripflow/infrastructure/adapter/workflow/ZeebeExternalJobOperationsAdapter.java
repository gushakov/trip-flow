package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.TripFlowBpmnError;
import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationError;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationsOutputPort;
import com.github.tripflow.infrastructure.adapter.workflow.map.JobTaskMapper;
import com.github.tripflow.infrastructure.error.AbstractErrorHandler;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Secondary adapter implementing {@link ExternalJobOperationsOutputPort} used to
 * perform any operations related to the {@link ActivatedJob} instance provided
 * during construction time.
 *
 * @see ZeebeExternalJobHandlingAdapter
 * @see JobClient
 * @see ActivatedJob
 */
@RequiredArgsConstructor
public class ZeebeExternalJobOperationsAdapter extends AbstractErrorHandler implements ExternalJobOperationsOutputPort {

    private final JobClient jobClient;
    private final ActivatedJob job;

    private final JobTaskMapper jobTaskMapper;

    @Override
    public TripTask activeTripTask() {
        try {
            return jobTaskMapper.convert(job);
        } catch (Exception e) {
            throw new ExternalJobOperationError("Cannot complete service task, job ID: %s"
                    .formatted(job.getElementId()), e);
        }
    }

    @Override
    public void throwError(TripFlowBpmnError error) {

        // log error and roll back transaction
        logErrorAndRollback(error);

        try {
            jobClient.newThrowErrorCommand(job)
                    .errorCode(error.getBpmnCode())
                    .errorMessage(error.getMessage())
                    .send();
        } catch (Exception e) {
            throw new ExternalJobOperationError("Cannot complete service task, job ID: %s"
                    .formatted(job.getElementId()), e);
        }
    }

    @Override
    public void completeCreditCheck(boolean sufficientCredit) {

        try {
            jobClient.newCompleteCommand(job)
                    .variables(Map.of(Constants.SUFFICIENT_CREDIT_VARIABLE, sufficientCredit))
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExternalJobOperationError("Cannot complete service task, job ID: %s"
                    .formatted(job.getElementId()), e);
        }

    }

    @Override
    public void completeTask() {
        try {
            jobClient.newCompleteCommand(job).send();
        } catch (Exception e) {
            throw new ExternalJobOperationError("Cannot complete service task, job ID: %s"
                    .formatted(job.getElementId()), e);
        }
    }
}
