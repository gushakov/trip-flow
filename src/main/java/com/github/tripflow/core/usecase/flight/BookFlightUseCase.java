package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.Validator;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.flight.BookFlightPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class BookFlightUseCase implements BookFlightInputPort {

    private final BookFlightPresenterOutputPort presenter;

    private final SecurityOperationsOutputPort securityOps;
    private final TasksOperationsOutputPort tasksOps;
    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void proposeFlightsForSelectionByCustomer(String taskId) {
        List<Flight> flights;
        TripTask tripTask;
        Trip trip;
        try {
            // get the task from the workflow engine
            tripTask = tasksOps.retrieveActiveTaskForAssigneeCandidateGroup(taskId, securityOps.tripFlowAssigneeRole());

            // load the trip
            trip = dbOps.loadTrip(tripTask.getTripId());

            // load all flights
            flights = dbOps.loadAllFlights();
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentFlightsForSelectionByCustomer(taskId, trip, flights);

    }

    @Override
    @Transactional
    public void registerSelectedFlightWithTrip(String taskId, TripId tripId, FlightNumber flightNumber) {
        Trip trip;
        try {
            trip = dbOps.loadTrip(tripId);
            dbOps.updateTrip(trip.bookFlight(flightNumber));
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfRegisteringSelectedFlightWithTrip(taskId, tripId, flightNumber);
    }

    @Override
    public void confirmFlightBooking(String taskId) {

        try {
            // get the task
            TripTask tripTask = tasksOps.retrieveActiveTaskForAssigneeCandidateGroup(taskId, securityOps.tripFlowAssigneeRole());

            // get the trip
            Trip trip = dbOps.loadTrip(tripTask.getTripId());

            /*
                Checking the flow-aggregate invariant here: we proceed with the flow
                only if the state of the flow (encapsulated with "tripTask") and the
                state of the corresponding "trip" aggregate are coherent.
             */
            Validator.assertThatFlightCanBeBooked(tripTask, trip);

            // complete the task
            tasksOps.completeFlightBookingTask(taskId);

        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentSuccessfulResultOfCompletingFlightBooking(taskId);
    }

}
