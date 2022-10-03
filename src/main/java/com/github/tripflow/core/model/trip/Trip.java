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

/**
 * Main aggregate representing trip planning. By design, the ID
 * of each {@code Trip} is the value of the key of the corresponding
 * process instance.
 */

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

    public boolean doesNotHaveFlightBookingAssigned() {
        return flightNumber == null;
    }

    public Trip assignHotelReservation(HotelId hotelId) {
        return newTrip().hotelId(hotelId).build();
    }

    public boolean hasHotelReservationAssigned() {
        return hotelId != null;
    }

    public Trip confirmFlightBooking() {
        // a flight booking must be assigned
        if (doesNotHaveFlightBookingAssigned()) {
            throw new TripFlowValidationError("A flight booking must be assigned prior to confirmation, trip ID: %s"
                    .formatted(tripId.getId()));
        }
        return newTrip().flightBooked(true).build();
    }

    public Trip confirmHotelReservation() {
        // a hotel reservation must be assigned
        if (!hasHotelReservationAssigned()) {
            throw new TripFlowValidationError("A hotel reservation must be assigned prior to confirmation, trip ID: %s"
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
