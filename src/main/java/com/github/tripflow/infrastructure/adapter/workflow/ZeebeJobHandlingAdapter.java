package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.usecase.confirmation.ConfirmTripInputPort;
import com.github.tripflow.core.usecase.creditcheck.CheckCreditInputPort;
import com.github.tripflow.infrastructure.adapter.db.DbPersistenceGateway;
import com.github.tripflow.infrastructure.adapter.workflow.map.JobTaskMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Primary adapter which uses Zeebe worker to handle job requests from the workflow engine.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ZeebeJobHandlingAdapter {

    private final ApplicationContext applicationContext;

    private final JobTaskMapper jobTaskMapper;

    private final DbPersistenceGateway dbGateway;

    @ZeebeWorker(type = Constants.USER_TASK_TYPE, forceFetchAllVariables = true)
    public void handleUserTask(final ActivatedJob job) {
        TripTask tripTask = jobTaskMapper.convertUserTask(job);
        log.debug("[Zeebe worker] Handling user task: {}, job key: {}", tripTask.getName(), job.getKey());
        dbGateway.saveTripTaskIfNeeded(tripTask);
        // for user tasks, we do not call any use case here: the use case will
        // be called eventually from the MVC controller responsible for servicing
        // the UI of the user task
    }

    @ZeebeWorker(type = "checkCredit", forceFetchAllVariables = true)
    public void handleCheckCreditTask(final ActivatedJob job) {
        TripTask tripTask = jobTaskMapper.convertServiceTask(job);
        log.debug("[Zeebe worker] Handling service task: {}, job key: {}", tripTask.getName(), job.getKey());
        dbGateway.saveTripTaskIfNeeded(tripTask);
        // for service tasks, we call the use case directly,
        // transferring control to it
        checkCreditUseCase().checkCreditLimit(tripTask.getTaskId());
    }

    @ZeebeWorker(type = "confirmTrip", forceFetchAllVariables = true)
    public void handleConfirmTripTask(final ActivatedJob job) {
        TripTask tripTask = jobTaskMapper.convertServiceTask(job);
        log.debug("[Zeebe worker] Handling service task: {}, job key: {}", tripTask.getName(), job.getKey());
        dbGateway.saveTripTaskIfNeeded(tripTask);
        confirmTripUseCase().confirmTrip(tripTask.getTaskId());
    }

    @ZeebeWorker(type = "refuseTrip", forceFetchAllVariables = true)
    public void handleRefuseTripTask(final ActivatedJob job) {
        TripTask tripTask = jobTaskMapper.convertServiceTask(job);
        log.debug("[Zeebe worker] Handling service task: {}, job key: {}", tripTask.getName(), job.getKey());
        dbGateway.saveTripTaskIfNeeded(tripTask);
        confirmTripUseCase().refuseTrip(tripTask.getTaskId());
    }

    private CheckCreditInputPort checkCreditUseCase() {
        return applicationContext.getBean(CheckCreditInputPort.class);
    }

    private ConfirmTripInputPort confirmTripUseCase() {
        return applicationContext.getBean(ConfirmTripInputPort.class);
    }

}
