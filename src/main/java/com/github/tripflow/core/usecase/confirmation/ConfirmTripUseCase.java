package com.github.tripflow.core.usecase.confirmation;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.TripFlowBpmnError;
import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationsOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfirmTripUseCase implements ConfirmTripInputPort {

    private final ExternalJobOperationsOutputPort externalJobOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void confirmTrip(TripId tripId) {
        try {
            Trip trip = dbOps.loadTrip(tripId);
            Trip confirmedTrip = trip.confirmTrip();
            dbOps.updateTrip(confirmedTrip);
        } catch (GenericTripFlowError e) {
            externalJobOps.throwError(new TripFlowBpmnError(Constants.EXTERNAL_JOB_ERROR_CODE, e.getMessage()));
        }
    }

    @Override
    public void cancelTrip(TripId tripId) {
        try {
            Trip trip = dbOps.loadTrip(tripId);
            Trip canceledTrip = trip.cancelTrip();
            dbOps.updateTrip(canceledTrip);
        } catch (GenericTripFlowError e) {
            externalJobOps.throwError(new TripFlowBpmnError(Constants.EXTERNAL_JOB_ERROR_CODE, e.getMessage()));
        }
    }
}
