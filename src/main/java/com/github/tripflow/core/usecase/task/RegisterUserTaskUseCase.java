package com.github.tripflow.core.usecase.task;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterUserTaskUseCase implements RegisterUserTaskInputPort {

    private final ErrorHandlingPresenterOutputPort presenter;
    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void registerActivatedUserTask(TripTask userTask) {
        try {
            // Workflow engine will most likely try to execute
            // this until the job is complete. So we can skip
            // saving the task if it is already there.
            if (!dbOps.tripTaskExists(userTask.getTaskId())){
                dbOps.saveTripTask(userTask);
            }
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
        }
    }
}
