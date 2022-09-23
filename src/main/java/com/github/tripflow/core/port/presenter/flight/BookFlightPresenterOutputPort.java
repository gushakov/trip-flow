package com.github.tripflow.core.port.presenter.flight;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.error.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BookFlightPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentFlightsForSelectionByCustomer(TripId tripId, List<Flight> flights);
}
