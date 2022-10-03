package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationError;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationsOutputPort;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Secondary adapter implementing {@link ExternalJobOperationsOutputPort} used to
 * perform any operations related to the {@link ActivatedJob} (external service)
 * instance provided during construction time.
 *
 * @see ZeebeWorkerJobHandlingAdapter
 * @see JobClient
 * @see ActivatedJob
 */
@RequiredArgsConstructor
public class ZeebeExternalJobOperationsAdapter implements ExternalJobOperationsOutputPort {

    private final JobClient jobClient;
    private final ActivatedJob job;

    @Override
    public void completeCreditCheck(boolean sufficientCredit) {

        try {
            jobClient.newCompleteCommand(job)
                    .variables(Map.of(Constants.SUFFICIENT_CREDIT_VARIABLE, sufficientCredit))
                    .send();
        } catch (Exception e) {
            throw new ExternalJobOperationError("Cannot complete external job: check credit", e);
        }

    }
}
