package com.github.tripflow.core.usecase.browse;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.browse.BrowseActiveTripsPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BrowseActiveTripsUseCase implements BrowseActiveTripsInputPort {

    private final BrowseActiveTripsPresenterOutputPort presenter;

    private final SecurityOperationsOutputPort securityOps;

    private final TasksOperationsOutputPort tasksOps;

    @Override
    public void listActiveTasksForTripsStartedByUser() {

        List<TripTask> tasks;
        try {
            // get the list of all active tasks assigned the candidate group (or role)
            // of the user and filter only the tasks for the trips started by the user
            tasks = tasksOps.listActiveTasksForAssigneeCandidateGroup(securityOps.tripFlowAssigneeRole())
                    .stream()
                    .filter(tripTask -> tripTask.getTripStartedBy().equals(securityOps.loggedInUserName()))
                    .toList();

        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentActiveTasksForTripsStartedByUser(tasks);
    }
}
