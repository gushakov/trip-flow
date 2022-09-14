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
    public void listTripsByActiveTasksAssignedToUser() {

        /*
            Get the list of all active tasks assigned to the current user
            in all active TripFlow instances.
         */
        List<TripTask> tasks;
        try {
            tasks = tasksOps.listActiveTasksForAssignee(securityOps.tripFlowAssigneeRole());
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentListOfTripsByActiveTasksAssignedToUser(tasks);
    }
}
