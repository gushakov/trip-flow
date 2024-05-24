package com.github.tripflow.core.usecase.hotel;

import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.workflow.WorkflowOperationsOutputPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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

        try {
            TripTask tripTask;
            Trip trip;
            List<Hotel> hotels;
            String city;

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

            presenter.presentHotelsInDestinationCityForSelectionByCustomer(taskId, trip, city, hotels);

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void registerSelectedHotelWithTrip(String taskId, TripId tripId, HotelId hotelId) {

        try {
            Trip trip = dbOps.loadTrip(tripId);
            dbOps.updateTrip(trip.assignHotelReservation(hotelId));
            presenter.presentResultOfRegisteringSelectedHotelWithTrip(taskId, tripId, hotelId);
        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void confirmHotelReservation(Long taskId) {
        try {
            Optional<TripTask> nextTripTaskOpt;
            TripId tripId;

            // get the task
            String candidateGroups = securityOps.tripFlowAssigneeRole();
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
            nextTripTaskOpt = dbOps.findTasksForTripAndUserWithRetry(trip.getTripId(),
                            candidateGroups, tripTask.getTripStartedBy())
                    .stream().findAny();

            if (nextTripTaskOpt.isPresent()) {
                presenter.presentResultOfConfirmingHotelReservationWithNextActiveTask(nextTripTaskOpt.get());
            } else {
                presenter.presentResultOfConfirmingHotelReservationWithoutNextActiveTask(tripId);
            }

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }
}
