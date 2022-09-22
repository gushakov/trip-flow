package com.github.tripflow.core.port.operation.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;

import java.util.List;

public interface DbPersistenceOperationsOutputPort {

    Trip saveTrip(Trip trip);

    Trip loadTrip(TripId tripId);

    List<Flight> loadAllFlights();

}
