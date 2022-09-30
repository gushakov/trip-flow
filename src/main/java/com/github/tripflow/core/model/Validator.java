package com.github.tripflow.core.model;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripTask;

import java.util.Optional;

public class Validator {

    /**
     * Throw {@code IllegalArgumentException} if {@code something} argument is {@code null}.
     *
     * @param something any object
     * @param <T>       any type
     * @return argument object if not {@code null}
     * @throws TripFlowValidationError if argument is {@code null}
     */
    public static <T> T notNull(T something) {
        return Optional.ofNullable(something).orElseThrow(() -> new TripFlowValidationError("Argument cannot be null"));
    }

    public static void assertThatFlightCanBeBooked(TripTask tripTask, Trip trip) {

        assertTaskTripIdEquivalence(tripTask, trip);

        // there should not be any flight already booked
        if (tripTask.isFlightBooked()){
            throw new TripFlowValidationError("A flight is already booked, task ID: %s"
                    .formatted(tripTask.getTaskId()));
        }

        // trip must have a flight selected
        if (!trip.hasFlightSelected()){
            throw new TripFlowValidationError("Trip %s does not have any flight selected"
                    .formatted(trip.getTripId()));
        }

    }

    public static void assertThatHotelCanBeReserved(TripTask tripTask, Trip trip, Flight flight, Hotel hotel) {

        assertTaskTripIdEquivalence(tripTask, trip);

        // there must be a flight booked
        if (!tripTask.isFlightBooked()){
            throw new TripFlowValidationError("A flight must be booked prior to any hotel reservation, task ID: %s"
                    .formatted(tripTask.getTaskId()));
        }

        // there should not be any hotel already reserved
        if (tripTask.isHotelReserved()){
            throw new TripFlowValidationError("A hotel is already reserved, task ID: %s"
                    .formatted(tripTask.getTaskId()));
        }

        // the destination city for the flight and the city of the hotel must match
        if (!flight.getDestinationCity().equals(hotel.getCity())){
            throw new TripFlowValidationError("Cannot reserve a hotel in city: %s, different from the flight destination city: %s"
                    .formatted(hotel.getCity(), flight.getDestinationCity()));
        }
    }

    private static void assertTaskTripIdEquivalence(TripTask tripTask, Trip trip) {
        // task must be for the right trip
        if (!tripTask.getTripId().equals(trip.getTripId())){
            throw new TripFlowValidationError("Task %s is not for the trip %s"
                    .formatted(tripTask.getTripId(), trip.getTripId()));
        }
    }
}
