package com.github.tripflow.core.port.operation.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;

import java.util.List;

public interface DbPersistenceOperationsOutputPort {

    Trip saveNewTrip(Trip trip);

    Trip updateTrip(Trip trip);

    Trip loadTrip(TripId tripId);

    List<Flight> loadAllFlights();

    List<Hotel> hotelsInCity(String city);

    Flight loadFlight(FlightNumber flightNumber);
}
