package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BookFlightPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentFlightsForSelectionByCustomer(Long taskId, Trip trip, List<Flight> flights);

    void presentResultOfRegisteringSelectedFlightWithTrip(String taskId, TripId tripId, FlightNumber flightNumber);

    void presentResultOfConfirmingFlightWithoutNextActiveTask(TripId tripId);

    void presentResultOfConfirmingFlightWithNextActiveTask(TripTask tripTask);
}
