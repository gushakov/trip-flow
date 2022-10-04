package com.github.tripflow.core.usecase.task;

import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.infrastructure.error.AbstractErrorHandler;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class HandleUserTaskUseCase extends AbstractErrorHandler implements HandleUserTaskInputPort {

    private final DbPersistenceOperationsOutputPort dbOps;

    @Transactional
    @Override
    public void handleUserTask(TripTask userTask) {
        try {
            dbOps.saveTripTask(userTask);
        } catch (Exception e) {
            logErrorAndRollback(e);
        }
    }
}
