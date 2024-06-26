package com.github.tripflow.core.usecase.confirmation;

import com.github.tripflow.core.TripFlowBpmnError;
import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.workflow.WorkflowOperationsOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfirmTripUseCase implements ConfirmTripInputPort {

    private final WorkflowOperationsOutputPort workflowOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void confirmTrip(Long taskId) {
        try {
            // get the task for the job
            TripTask tripTask = dbOps.loadTripTask(taskId);
            TripId tripId = tripTask.getTripId();

            // load the trip
            Trip trip = dbOps.loadTrip(tripId);

            // confirm and update the trip
            Trip confirmedTrip = trip.confirmTrip();

            dbOps.updateTrip(confirmedTrip);

            // complete the task
            workflowOps.completeTask(taskId);
        } catch (Exception e) {
            workflowOps.throwBpmnError(taskId, new TripFlowBpmnError(Constants.EXTERNAL_JOB_ERROR_CODE, e.getMessage()));
        }
    }

    @Override
    public void refuseTrip(Long taskId) {
        try {
            // get the task for the job
            TripTask tripTask = dbOps.loadTripTask(taskId);
            TripId tripId = tripTask.getTripId();

            // load the trip
            Trip trip = dbOps.loadTrip(tripId);

            // refuse and update the trip
            Trip refusedTrip = trip.refuseTrip();
            dbOps.updateTrip(refusedTrip);

            // complete the task
            workflowOps.completeTask(taskId);
        } catch (Exception e) {
            workflowOps.throwBpmnError(taskId, new TripFlowBpmnError(Constants.EXTERNAL_JOB_ERROR_CODE, e.getMessage()));
        }
    }
}
