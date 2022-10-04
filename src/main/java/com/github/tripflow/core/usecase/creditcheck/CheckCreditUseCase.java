package com.github.tripflow.core.usecase.creditcheck;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.TripFlowBpmnError;
import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.config.ConfigurationOperationsOutputPort;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.TripFlowSecurityError;
import com.github.tripflow.core.port.operation.workflow.ExternalJobOperationsOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckCreditUseCase implements CheckCreditInputPort {

    private final ExternalJobOperationsOutputPort externalJobOps;

    private final SecurityOperationsOutputPort securityOps;

    private final ConfigurationOperationsOutputPort configOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void checkCreditLimit() {

        try {

            // get task for the active job
            TripTask tripTask = externalJobOps.activeTripTask();
            TripId tripId = tripTask.getTripId();

            // get the username of the user who started the trip
            String tripStartedBy = tripTask.getTripStartedBy();

            // check that the user who started the trip is a customer
            if (!securityOps.isUserCustomer(tripStartedBy)) {
                throw new TripFlowSecurityError("User %s does not have a \"customer\" role"
                        .formatted(tripStartedBy));
            }

            // get the credit limit for the customer
            int creditLimit = configOps.obtainCreditLimitForCustomer(tripStartedBy);

            // load the trip, flight, hotel and check invariants
            Trip trip = dbOps.loadTrip(tripId);

            // a flight must be booked
            if (!trip.isFlightBooked()) {
                throw new TripFlowValidationError("Trip must have a flight booked prior to credit check, trip ID: %s"
                        .formatted(tripId));
            }
            Flight flight = dbOps.loadFlight(trip.getFlightNumber());

            // a hotel must be reserved
            if (!trip.isHotelReserved()) {
                throw new TripFlowValidationError("Trip must have a hotel reserved prior to credit check, trip ID: %s"
                        .formatted(tripId));
            }
            Hotel hotel = dbOps.loadHotel(trip.getHotelId());

            // calculate the total price for the trip
            int totalPrice = flight.getPrice() + hotel.getPrice();

            // check if the customer has a sufficient credit, completing the task
            // with the appropriate variable value

            externalJobOps.completeCreditCheck(totalPrice <= creditLimit);
        } catch (GenericTripFlowError e) {
            e.printStackTrace();
            externalJobOps.throwError(new TripFlowBpmnError(Constants.EXTERNAL_JOB_ERROR_CODE, e.getMessage()));
        }

    }
}
