package com.github.tripflow.core.port.operation.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;

import java.util.List;
import java.util.Optional;

public interface DbPersistenceOperationsOutputPort {

    Trip saveNewTrip(Trip trip);

    Trip updateTrip(Trip trip);

    Trip loadTrip(TripId tripId);

    List<Flight> loadAllFlights();

    List<Hotel> hotelsInCity(String city);

    Flight loadFlight(FlightNumber flightNumber);

    Hotel loadHotel(HotelId hotelId);

    List<Trip> findOpenTripsStartedByUser(String startedBy);

    void saveTripTask(TripTask tripTask);

    TripTask loadTripTask(Long taskId);

    Optional<TripTask> findAnyActivatedTaskForTripStartedByUser(TripId tripId, String tripStartedBy);

    List<TripTask> listAnyActivatedTripTasksStartedByUser(String tripStartedBy);
}
