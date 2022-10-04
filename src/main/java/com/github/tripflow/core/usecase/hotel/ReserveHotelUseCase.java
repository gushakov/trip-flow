package com.github.tripflow.core.usecase.hotel;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.core.port.presenter.hotel.ReserveHotelPresenterOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReserveHotelUseCase implements ReserveHotelInputPort {

    ReserveHotelPresenterOutputPort presenter;

    private SecurityOperationsOutputPort securityOps;
    DbPersistenceOperationsOutputPort dbOps;

    WorkflowOperationsOutputPort workflowOps;

    @Override
    public void proposeHotelsForSelectionByCustomer(Long taskId) {

        TripTask tripTask;
        Trip trip;
        List<Hotel> hotels;
        String city;
        try {
            // get the task from the workflow engine
            tripTask = dbOps.loadTripTask(taskId);

            // load the trip
            trip = dbOps.loadTrip(tripTask.getTripId());

            // check that trip has a flight selected
            if (trip.doesNotHaveFlightBookingAssigned()) {
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
            dbOps.updateTrip(trip.assignHotelReservation(hotelId));
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfRegisteringSelectedHotelWithTrip(taskId, tripId, hotelId);
    }

    @Transactional
    @Override
    public void confirmHotelReservation(Long taskId) {
        Optional<TripTask> nextTripTaskOpt;
        TripId tripId;
        try {

            // get the task
            String assigneeRole = securityOps.tripFlowAssigneeRole();
            TripTask tripTask = dbOps.loadTripTask(taskId);

            // get the trip
            tripId = tripTask.getTripId();
            Trip trip = dbOps.loadTrip(tripId);

            // get the flight
            Flight flight = dbOps.loadFlight(trip.getFlightNumber());

            // get the hotel
            Hotel hotel = dbOps.loadHotel(trip.getHotelId());

            // We need to check some inter-aggregate invariants here.

            // flight destination city must be the same as hotel city
            if (!flight.getDestinationCity().equals(hotel.getCity())) {
                throw new TripFlowValidationError("The destination city of the flight: %s not equal to the city of the hotel: %s"
                        .formatted(flight.getDestinationCity(), hotel.getCity()));
            }

            // confirm hotel reservation and update Trip aggregate
            Trip withConfirmedHotelReservation = trip.confirmHotelReservation();
            dbOps.updateTrip(withConfirmedHotelReservation);

            // complete the workflow task
            workflowOps.completeTask(taskId);

            // advance to the next task
            nextTripTaskOpt = dbOps.findAnyActivatedTaskForTripStartedByUser(trip.getTripId(), tripTask.getTripStartedBy());

        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        if (nextTripTaskOpt.isPresent()) {
            presenter.presentResultOfConfirmingHotelReservationWithNextActiveTask(nextTripTaskOpt.get());
        } else {
            presenter.presentResultOfConfirmingHotelReservationWithoutNextActiveTask(tripId);
        }
    }
}
