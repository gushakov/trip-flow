package com.github.tripflow.infrastructure.adapter.workflow;

/*
    References:
    ----------

    1. Zeebe worker, example: https://github.com/camunda-community-hub/spring-zeebe/blob/master/examples/worker/src/main/java/io/camunda/zeebe/spring/example/WorkerApplication.java
 */


import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.usecase.creditcheck.CheckCreditInputPort;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Primary adapter which will be called by the workflow engine to execute
 * any service task with matching definition.
 *
 * @see ZeebeExternalJobOperationsAdapter
 * @see ZeebeWorker
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ZeebeWorkerJobHandlingAdapter {

    private final ApplicationContext applicationContext;

    /*
        The "type" argument, must match the definition of an existing service task in the BPMN.
        Notice that we are not setting "autoComplete" to "true". This way it's the use case which
        will decide whether to complete the job or throw any exceptions.
     */

    @ZeebeWorker(type = "checkCredit", forceFetchAllVariables = true)
    public void checkCredit(final JobClient jobClient, final ActivatedJob job) {
        log.debug("[Zeebe Worker][External Job] Executing external job: checkCredit for BPMN process instance with ID: {}",
                job.getProcessInstanceKey());

        // get "tripId" process instance variable
        TripId tripId = TripId.of((Long) job.getVariablesAsMap().get(Constants.TRIP_ID_PROCESS_VARIABLE));

        useCase(jobClient, job).checkCreditLimit(tripId);
    }

    private CheckCreditInputPort useCase(JobClient jobClient, ActivatedJob job) {
        return applicationContext.getBean(CheckCreditInputPort.class, new ZeebeExternalJobOperationsAdapter(jobClient, job));
    }

}
