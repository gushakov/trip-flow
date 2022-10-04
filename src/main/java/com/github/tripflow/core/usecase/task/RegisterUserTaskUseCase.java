package com.github.tripflow.core.usecase.task;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class RegisterUserTaskUseCase implements RegisterUserTaskInputPort {

    private final ErrorHandlingPresenterOutputPort presenter;
    private final DbPersistenceOperationsOutputPort dbOps;

    @Transactional
    @Override
    public void registerActivatedUserTask(TripTask userTask) {
        try {
            // just save user task in the database
            dbOps.saveTripTask(userTask);
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
        }
    }
}
