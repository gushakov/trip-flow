package com.github.tripflow.core.model.trip;

import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.hotel.HotelId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.github.tripflow.core.model.Validator.notNull;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trip {

    @EqualsAndHashCode.Include
    TripId tripId;

    String startedBy;

    FlightNumber flightNumber;

    HotelId hotelId;
    boolean flightBooked;

    boolean hotelReserved;

    boolean tripCancelled;

    boolean tripConfirmed;

    @Builder
    public Trip(TripId tripId, String startedBy,
                FlightNumber flightNumber, HotelId hotelId,
                boolean flightBooked, boolean hotelReserved,
                boolean tripCancelled, boolean tripConfirmed) {
        // these are never null
        this.tripId = notNull(tripId);
        this.startedBy = notNull(startedBy);

        // there will be false by default
        this.flightBooked = flightBooked;
        this.hotelReserved = hotelReserved;
        this.tripCancelled = tripCancelled;
        this.tripConfirmed = tripConfirmed;

        // these can be null
        this.flightNumber = flightNumber;
        this.hotelId = hotelId;
    }

    public Trip assignFlightBooking(FlightNumber number) {
        return newTrip().flightNumber(number).build();
    }

    /**
     * Will indicate that a flight booking was assigned to the trip, but not necessarily
     * confirmed yet.
     * @return {@code true} if a flight was selected for the trip
     */
    public boolean hasFlightBookingAssigned() {
        return flightNumber != null;
    }

    public Trip assignHotelReservation(HotelId hotelId) {
        return newTrip().hotelId(hotelId).build();
    }

    /**
     * Will indicate that a hotel reservation was assigned to the trip, but not necessarily
     * confirmed yet.
     * @returnco {@code true} if a hotel was selected for the trip
     */
    public boolean hasHotelReservationAssigned() {
        return hotelId != null;
    }

    public Trip confirmFlightBooking() {
        // a flight booking must be assigned
        if (!hasFlightBookingAssigned()) {
            throw new TripFlowValidationError("A flight booking must be assigned prior to confirmation, trip ID: %s"
                    .formatted(tripId.getId()));
        }
        return newTrip().flightBooked(true).build();
    }

    public Trip confirmHotelReservation(HotelId hotelId) {
        // a hotel reservation must be assigned
        if (!hasHotelReservationAssigned()) {
            throw new TripFlowValidationError("A hotel reservation must be assigned prior to confiration, trip ID: %s"
                    .formatted(tripId.getId()));
        }
        return newTrip().hotelReserved(true).build();
    }

    private TripBuilder newTrip() {
        return new TripBuilder()
                .tripId(tripId)
                .startedBy(startedBy)
                .flightNumber(flightNumber)
                .hotelId(hotelId)
                .flightBooked(flightBooked)
                .hotelReserved(hotelReserved)
                .tripCancelled(tripCancelled)
                .tripConfirmed(tripConfirmed);

    }
}
