package com.github.tripflow.core.port.presenter.flight;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.port.error.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface BookFlightPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentFlightsForSelectionByCustomer(Trip trip, List<Flight> flights);
}
