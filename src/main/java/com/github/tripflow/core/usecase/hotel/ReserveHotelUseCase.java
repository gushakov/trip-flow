package com.github.tripflow.core.usecase.hotel;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.hotel.ReserveHotelPresenterOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.transaction.Transactional;
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

            // check flow-aggregate invariant
            if (!trip.hasFlightSelected()) {
                throw new TripFlowValidationError("Trip %s does not have any flight selected"
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

        presenter.presentHotelsInDestinationCityForSelectionByCustomer(taskId, trip, city, hotels);

    }

    @Override
    @Transactional
    public void registerSelectedHotelWithTrip(String taskId, TripId tripId, HotelId hotelId) {

        Trip trip;
        try {
            trip = dbOps.loadTrip(tripId);
            dbOps.updateTrip(trip.reserveHotel(hotelId));
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfRegisteringSelectedHotelWithTrip(taskId, tripId, hotelId);
    }
}
