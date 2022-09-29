package com.github.tripflow.core.usecase.hotel;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.InconsistentWorkflowStateError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.hotel.ReserveHotelPresenterOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
        List<Hotel> hotels;
        String city;
        try {
            // get the task from the workflow engine
            tripTask = tasksOps.retrieveActiveTaskForAssigneeCandidateGroup(taskId, securityOps.tripFlowAssigneeRole());

            // load the trip
            trip = dbOps.loadTrip(tripTask.getTripId());

            // check that we are in a consistent state: we must have a
            // flight registered with the trip
            if (!tripTask.isFlightBooked() || !trip.hasFlightBooked()) {
                throw new InconsistentWorkflowStateError("Trip with ID: %s does not yet have a flight registered."
                        .formatted(trip.getTripId()));
            }

            // load the flight for the trip
            Flight flight = dbOps.loadFlight(trip.getFlightNumber());

            // find all hotels for the destination city
            city = flight.getDestinationCity();
            hotels = dbOps.hotelsInCity(city);

        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentHotelsInDestinationCityForSelectionByCustomer(taskId, trip.getTripId(), city, hotels);

    }
}
