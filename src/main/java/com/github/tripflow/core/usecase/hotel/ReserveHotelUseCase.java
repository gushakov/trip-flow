package com.github.tripflow.core.usecase.hotel;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.hotel.ReserveHotelPresenterOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReserveHotelUseCase implements ReserveHotelInputPort {

    ReserveHotelPresenterOutputPort presenter;

    private SecurityOperationsOutputPort securityOps;
    TasksOperationsOutputPort tasksOps;

    DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void proposeHotelsForSelectionByCustomer(String taskId) {

        TripTask tripTask;
        Trip trip;
        try {
            // get the task from the workflow engine
            tripTask = tasksOps.retrieveActiveTaskForAssigneeCandidateGroup(taskId, securityOps.tripFlowAssigneeRole());

            // load the trip
            trip = dbOps.loadTrip(tripTask.getTripId());


        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }



    }
}
