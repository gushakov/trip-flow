package com.github.tripflow.infrastructure.adapter.workflow;

/*
    References:
    ----------

    1. Zeebe worker, example: https://github.com/camunda-community-hub/spring-zeebe/blob/master/examples/worker/src/main/java/io/camunda/zeebe/spring/example/WorkerApplication.java
 */


import com.github.tripflow.core.usecase.confirmation.ConfirmTripInputPort;
import com.github.tripflow.core.usecase.creditcheck.CheckCreditInputPort;
import com.github.tripflow.infrastructure.adapter.workflow.map.JobTaskMapper;
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
public class ZeebeExternalJobHandlingAdapter {

    private final ApplicationContext applicationContext;

    private final JobTaskMapper jobTaskMapper;

    /*
        The "type" argument, must match the definition of an existing service task in the BPMN.
        Notice that we are not setting "autoComplete" to "true". This way, it is the use case which
        will decide whether to complete the job or throw any exceptions.
     */

    @ZeebeWorker(type = "checkCredit", forceFetchAllVariables = true)
    public void checkCredit(final JobClient jobClient, final ActivatedJob job) {
        log.debug("[Zeebe Worker][External Job] Executing external job: {} for BPMN process instance with ID: {}",
               job.getType(), job.getProcessInstanceKey());

        checkCreditUseCase(jobClient, job).checkCreditLimit();
    }

    @ZeebeWorker(type = "io.camunda.zeebe:userTask", name = "",  forceFetchAllVariables = true)
    public void handleJobBookFlight(final JobClient jobClient, final ActivatedJob job) {
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>");
        System.out.println(">>>>>>>>>>"+job);
    }


    @ZeebeWorker(type = "confirmTrip", forceFetchAllVariables = true)
    public void confirmTrip(final JobClient jobClient, final ActivatedJob job) {
        log.debug("[Zeebe Worker][External Job] Executing external job: {} for BPMN process instance with ID: {}",
               job.getType(), job.getProcessInstanceKey());

        confirmTripUseCase(jobClient, job).confirmTrip();
    }

    @ZeebeWorker(type = "refuseTrip", forceFetchAllVariables = true)
    public void refuseTrip(final JobClient jobClient, final ActivatedJob job) {
        log.debug("[Zeebe Worker][External Job] Executing external job: {} for BPMN process instance with ID: {}",
               job.getType(), job.getProcessInstanceKey());

        confirmTripUseCase(jobClient, job).refuseTrip();
    }

    private CheckCreditInputPort checkCreditUseCase(JobClient jobClient, ActivatedJob job) {
        return applicationContext.getBean(CheckCreditInputPort.class,
                new ZeebeExternalJobOperationsAdapter(jobClient, job, jobTaskMapper));
    }

    private ConfirmTripInputPort confirmTripUseCase(JobClient jobClient, ActivatedJob job) {
        return applicationContext.getBean(ConfirmTripInputPort.class,
                new ZeebeExternalJobOperationsAdapter(jobClient, job, jobTaskMapper));
    }

}
