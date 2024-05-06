package com.github.tripflow.core.usecase.outcome;

import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.workflow.WorkflowOperationsOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class ViewOutcomeUseCase implements ViewOutcomeInputPort {

    private final ViewOutcomePresenterOutputPort presenter;

    private final DbPersistenceOperationsOutputPort dbOps;

    private final WorkflowOperationsOutputPort workflowOps;

    @Override
    public void viewOutcome(Long taskId) {
        Trip trip;
        Flight flight;
        try {
            TripTask tripTask = dbOps.loadTripTask(taskId);

            trip = dbOps.loadTrip(tripTask.getTripId());

            // check if trip is in a consistent state
            if (!trip.hasConsistentOutcome()) {
                throw new TripFlowValidationError("Booking of the trip is not in a consistent state, trip ID: %s"
                        .formatted(trip.getTripId()));
            }

            flight = dbOps.loadFlight(trip.getFlightNumber());

        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        if (trip.isTripConfirmed()) {
            presenter.presentSuccessfulTripConfirmation(taskId, trip.getTripId(), flight.getDestinationCity());
        } else {
            presenter.presentRefusedTripBooking(taskId, trip.getTripId(), flight.getDestinationCity());
        }
    }

    @Transactional
    @Override
    public void finishProcess(Long taskId) {
        Trip trip;
        try {
            TripTask tripTask = dbOps.loadTripTask(taskId);
            trip = dbOps.loadTrip(tripTask.getTripId());
            workflowOps.completeTask(taskId);
        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfSuccessfullyFinishingProcess(trip.getTripId());
    }
}
