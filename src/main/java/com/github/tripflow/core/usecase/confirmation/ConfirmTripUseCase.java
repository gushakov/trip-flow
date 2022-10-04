package com.github.tripflow.core.usecase.confirmation;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.TripFlowBpmnError;
import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationsOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class ConfirmTripUseCase implements ConfirmTripInputPort {

    private final ExternalJobOperationsOutputPort externalJobOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Transactional
    @Override
    public void confirmTrip() {
        try {
            // get the task for the job
            TripTask tripTask = externalJobOps.activeTripTask();
            TripId tripId = tripTask.getTripId();

            // load the trip
            Trip trip = dbOps.loadTrip(tripId);

            // confirm and update the trip
            Trip confirmedTrip = trip.confirmTrip();
            dbOps.updateTrip(confirmedTrip);

            // complete the task
            externalJobOps.completeTask();
        } catch (GenericTripFlowError e) {
            externalJobOps.throwError(new TripFlowBpmnError(Constants.EXTERNAL_JOB_ERROR_CODE, e.getMessage()));
        }
    }

    @Transactional
    @Override
    public void refuseTrip() {
        try {
            // get the task for the job
            TripTask tripTask = externalJobOps.activeTripTask();
            TripId tripId = tripTask.getTripId();

            // load the trip
            Trip trip = dbOps.loadTrip(tripId);

            // refuse and update the trip
            Trip canceledTrip = trip.refuseTrip();
            dbOps.updateTrip(canceledTrip);

            // complete the task
        } catch (GenericTripFlowError e) {
            externalJobOps.throwError(new TripFlowBpmnError(Constants.EXTERNAL_JOB_ERROR_CODE, e.getMessage()));
        }
    }
}
