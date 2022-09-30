package com.github.tripflow.core.usecase.summary;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.summary.ViewSummaryPresenterOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ViewSummaryUseCase implements ViewSummaryInputPort{

    private final ViewSummaryPresenterOutputPort presenter;
    private final SecurityOperationsOutputPort securityOps;
    private final TasksOperationsOutputPort tasksOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void viewTripSummary(String taskId) {
        Trip trip;

        try {
            TripTask tripTask = tasksOps.retrieveActiveTaskForAssigneeCandidateGroup(taskId, securityOps.tripFlowAssigneeRole());

            trip = dbOps.loadTrip(tripTask.getTripId());
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentTripSummary(trip);

    }
}
