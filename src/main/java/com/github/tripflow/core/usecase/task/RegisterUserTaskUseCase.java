package com.github.tripflow.core.usecase.task;

import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.infrastructure.error.AbstractErrorHandler;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class RegisterUserTaskUseCase extends AbstractErrorHandler implements RegisterUserTaskInputPort {

    private final DbPersistenceOperationsOutputPort dbOps;

    @Transactional
    @Override
    public void registerActivatedUserTask(TripTask userTask) {
        try {
            // just save user task in the database
            dbOps.saveTripTask(userTask);
        } catch (Exception e) {
            logErrorAndRollback(e);
        }
    }
}
