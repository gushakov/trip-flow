package com.github.tripflow.core.port.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripEntry;
import com.github.tripflow.core.model.trip.TripId;

import java.util.List;

public interface DbPersistenceOperationsOutputPort {

    /**
     * Executes provided {@linkplain Runnable} in a transaction configured
     * with default propagation strategy and isolation level.
     *
     * @param runnable runnable to execute
     * @throws TripFlowDbPersistenceError if there was a problem setting up or executing
     *                                    a transaction, all other {@linkplain RuntimeException}s
     *                                    which may be thrown by the {@code runnable} itself are
     *                                    propagated to the caller
     */
    void doInTransaction(Runnable runnable);

    void saveNewTrip(Trip trip);

    void updateTrip(Trip trip);

    Trip loadTrip(TripId tripId);

    List<Flight> loadAllFlights();

    List<Hotel> hotelsInCity(String city);

    Flight loadFlight(FlightNumber flightNumber);

    Hotel loadHotel(HotelId hotelId);

    void saveTripTaskIfNeeded(TripTask tripTask);

    TripTask loadTripTask(Long taskId);

    void removeTripTask(Long taskId);

    boolean tripTaskExists(Long taskId);

    List<TripTask> findTasksForTripAndUserWithRetry(TripId tripId, String candidateGroups, String tripStartedBy);

    List<TripTask> findTasksForUser(String candidateGroups, String tripStartedBy);

    List<TripEntry> findAllOpenTripsForUser(String candidateGroups, String tripStartedBy);

}
