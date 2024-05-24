package com.github.tripflow.core.usecase.summary;

import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripSummary;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.workflow.WorkflowOperationsOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ViewSummaryUseCase implements ViewSummaryInputPort {

    private final ViewSummaryPresenterOutputPort presenter;
    private final SecurityOperationsOutputPort securityOps;
    private final DbPersistenceOperationsOutputPort dbOps;

    private final WorkflowOperationsOutputPort workflowOps;

    @Override
    public void viewTripSummary(Long taskId) {
        try {
            Trip trip;
            Flight flight;
            Hotel hotel;

            TripTask tripTask = dbOps.loadTripTask(taskId);

            trip = dbOps.loadTrip(tripTask.getTripId());

            // check if the trip is in the consistent state, and
            // load flight and hotel data

            if (!trip.isFlightBooked()) {
                throw new TripFlowValidationError("Flight must be booked for trip %s prior to the trip's summary review"
                        .formatted(trip.getTripId()));
            }

            flight = dbOps.loadFlight(trip.getFlightNumber());

            if (!trip.isHotelReserved()) {
                throw new TripFlowValidationError("Hotel must be reserved for trip %s prior to the trip's summary review"
                        .formatted(trip.getTripId()));
            }

            hotel = dbOps.loadHotel(trip.getHotelId());

            // construct summary object for the trip and present it
            presenter.presentTripSummary(summarizeTrip(taskId, trip, flight, hotel));

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    @Override
    public void proceedWithPayment(Long taskId) {

        try {
            Optional<TripTask> nextTripTaskOpt;
            TripTask tripTask;

            tripTask = dbOps.loadTripTask(taskId);

            // complete view summary task
            workflowOps.completeTask(taskId);

            String candidateGroups = securityOps.tripFlowAssigneeRole();

            // advance to the next task
            nextTripTaskOpt = dbOps.findTasksForTripAndUserWithRetry(tripTask.getTripId(),
                            candidateGroups, tripTask.getTripStartedBy())
                    .stream().findAny();

            if (nextTripTaskOpt.isPresent()) {
                presenter.presentResultOfProceedingWithPaymentWithNextActiveTask(nextTripTaskOpt.get());
            } else {
                presenter.presentResultOfProceedingWithPaymentWithoutNextActiveTask(tripTask.getTripId());
            }

        } catch (Exception e) {
            presenter.presentError(e);
        }

    }

    private TripSummary summarizeTrip(Long taskId, Trip trip, Flight flight, Hotel hotel) {

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
