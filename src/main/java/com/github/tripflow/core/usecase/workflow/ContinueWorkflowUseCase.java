package com.github.tripflow.core.usecase.workflow;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.ContinueWorkflowPresenterOutputPort;
import com.github.tripflow.core.port.operation.workflow.TaskOperationError;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ContinueWorkflowUseCase implements ContinueWorkflowInputPort {

    private final ContinueWorkflowPresenterOutputPort presenter;

    private final SecurityOperationsOutputPort securityOp;

    private final TasksOperationsOutputPort tasksOps;

    @Override
    public void continueToNextActiveTaskForUser(TripId tripId) {

        Optional<TripTask> tripTaskOpt;

        try {
           tripTaskOpt = tasksOps.retrieveNextActiveTaskForUser(tripId,
                   securityOp.tripFlowAssigneeRole(),
                   securityOp.loggedInUserName());
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        if (tripTaskOpt.isPresent()){
            presenter.presentNextActiveTaskForUser(tripTaskOpt.get());
        }
        else {
            presenter.presentNoActiveTasksFoundForUserResult(tripId);
        }

    }
}
