package com.github.tripflow.core.model;

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

        // task must be for the right trip
        if (!tripTask.getTripId().equals(trip.getTripId())){
            throw new TripFlowValidationError("Task %s is not for the trip %s"
                    .formatted(tripTask.getTripId(), trip.getTripId()));
        }

        // there should be no flight already booked
        if (tripTask.isFlightBooked()){
            throw new TripFlowValidationError("A flight is already booked according to the task %s"
                    .formatted(tripTask.getTaskId()));
        }

        // trip must have a flight selected
        if (!trip.hasFlightSelected()){
            throw new TripFlowValidationError("Trip %s does not have any flight selected"
                    .formatted(trip.getTripId()));
        }

    }
}
