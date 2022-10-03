package com.github.tripflow.core.usecase.summary;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.summary.ViewSummaryPresenterOutputPort;
import com.github.tripflow.core.model.summary.TripSummary;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ViewSummaryUseCase implements ViewSummaryInputPort {

    private final ViewSummaryPresenterOutputPort presenter;
    private final SecurityOperationsOutputPort securityOps;
    private final TasksOperationsOutputPort tasksOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void viewTripSummary(String taskId) {
        Trip trip;
        Flight flight;
        Hotel hotel;
        try {
            TripTask tripTask = tasksOps.retrieveActiveTaskForAssigneeCandidateGroup(taskId, securityOps.tripFlowAssigneeRole());

            trip = dbOps.loadTrip(tripTask.getTripId());

            // check if the trip is in the consistent state, and
            // load flight and hotel data

            if (!trip.isFlightBooked()){
                throw new TripFlowValidationError("Flight must be booked for trip %s prior to the trip's summary review"
                        .formatted(trip.getTripId()));
            }

            flight = dbOps.loadFlight(trip.getFlightNumber());

            if (!trip.isHotelReserved()){
                throw new TripFlowValidationError("Hotel must be reserved for trip %s prior to the trip's summary review"
                        .formatted(trip.getTripId()));
            }

            hotel = dbOps.loadHotel(trip.getHotelId());

        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        // construct summary object for the trip and present it
        presenter.presentTripSummary(summarizeTrip(taskId, trip, flight, hotel));

    }

    private TripSummary summarizeTrip(String taskId, Trip trip, Flight flight, Hotel hotel){

        // Calculate total price for the trip. This is one of the reasons why
        // "TripSummary" is not a DTO constructed in the presenter: we may
        // have to perform some business logic here.

        int totalPrice = flight.getPrice() + hotel.getPrice();

        return TripSummary.builder()
                .taskId(taskId)
                .tripId(trip.getTripId())
                .flightNumber(flight.getFlightNumber())
                .flightOrigin(flight.getOriginCity())
                .flightDestination(flight.getDestinationCity())
                .flightPrice(flight.getPrice())
                .hotelName(hotel.getName())
                .hotelPrice(hotel.getPrice())
                .totalPrice(totalPrice)
                .build();
    }
}
