package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.workflow.WorkflowOperationsOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class BookFlightUseCase implements BookFlightInputPort {

    private final BookFlightPresenterOutputPort presenter;

    private final SecurityOperationsOutputPort securityOps;
    private final DbPersistenceOperationsOutputPort dbOps;

    private final WorkflowOperationsOutputPort workflowOps;

    @Override
    public void proposeFlightsForSelectionByCustomer(Long taskId) {
        List<Flight> flights;
        TripTask tripTask;
        Trip trip;
        try {
            // get the task from the workflow engine
            tripTask = dbOps.loadTripTask(taskId);

            // load the trip
            trip = dbOps.loadTrip(tripTask.getTripId());

            // load all flights
            flights = dbOps.loadAllFlights();
        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentFlightsForSelectionByCustomer(taskId, trip, flights);

    }

    @Override
    @Transactional
    public void registerSelectedFlightWithTrip(String taskId, TripId tripId, FlightNumber flightNumber) {
        Trip trip;
        boolean rollback = false;
        try {
            trip = dbOps.loadTrip(tripId);
            dbOps.updateTrip(trip.assignFlightBooking(flightNumber));
        } catch (Exception e) {
            rollback = true;
            presenter.presentError(e);
            return;
        } finally {
            if (rollback) {
                dbOps.rollback();
            }
        }

        presenter.presentResultOfRegisteringSelectedFlightWithTrip(taskId, tripId, flightNumber);
    }

    @Transactional
    @Override
    public void confirmFlightBooking(Long taskId) {
        Optional<TripTask> nextTripTaskOpt;
        TripId tripId;
        boolean rollback = false;
        try {
            // get the task
            String candidateGroups = securityOps.tripFlowAssigneeRole();
            TripTask tripTask = dbOps.loadTripTask(taskId);
            tripId = tripTask.getTripId();

            // get the trip
            Trip trip = dbOps.loadTrip(tripTask.getTripId());

            Trip tripWithConfirmedFlightBooking = trip.confirmFlightBooking();
            dbOps.updateTrip(tripWithConfirmedFlightBooking);

            // complete the task
            workflowOps.completeTask(taskId);

            // advance to the next task
            nextTripTaskOpt = dbOps.findTasksForTripAndUserWithRetry(trip.getTripId(),
                            candidateGroups, tripTask.getTripStartedBy())
                    .stream().findAny();

        } catch (Exception e) {
            rollback = true;
            presenter.presentError(e);
            return;
        } finally {
            if (rollback) {
                dbOps.rollback();
            }
        }

        if (nextTripTaskOpt.isPresent()) {
            presenter.presentResultOfConfirmingFlightWithNextActiveTask(nextTripTaskOpt.get());
        } else {
            presenter.presentResultOfConfirmingFlightWithoutNextActiveTask(tripId);
        }
    }

}
