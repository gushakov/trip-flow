package com.github.tripflow.core.model.trip;

import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.hotel.HotelId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

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

    TripStatus status;

    @Builder
    public Trip(TripId tripId, String startedBy, FlightNumber flightNumber, HotelId hotelId, TripStatus status) {
        // these are never null
        this.tripId = notNull(tripId);
        this.startedBy = notNull(startedBy);
        this.status = Optional.ofNullable(status).orElse(TripStatus.UNDEFINED);

        // these can be null
        this.flightNumber = flightNumber;
        this.hotelId = hotelId;
    }

    public boolean hasFlightBookingAssigned() {
        return flightNumber != null;
    }

    public Trip assignFlightBooking(FlightNumber number) {
        return newTrip().flightNumber(number).build();
    }

    public Trip assignHotelReservation(HotelId hotelId) {
        return newTrip().hotelId(hotelId).build();
    }

    public boolean hasHotelReservationAssigned() {
        return hotelId != null;
    }

    public Trip confirmFlightBooking() {
        // a flight booking must be assigned
        if (!hasFlightBookingAssigned()) {
            throw new TripFlowValidationError("A flight booking must be assigned prior to confirmation, trip ID: %s"
                    .formatted(tripId.getId()));
        }
        return newTrip().status(TripStatus.FLIGHT_BOOKED).build();
    }

    public Trip confirmHotelReservation(HotelId hotelId) {
        // a hotel reservation must be assigned
        if (!hasHotelReservationAssigned()) {
            throw new TripFlowValidationError("A hotel reservation must be assigned prior to confiration, trip ID: %s"
                    .formatted(tripId.getId()));
        }
        return newTrip().status(TripStatus.HOTEL_RESERVED).build();
    }

    private TripBuilder newTrip() {
        return new TripBuilder()
                .tripId(tripId)
                .startedBy(startedBy)
                .status(status)
                .flightNumber(flightNumber)
                .hotelId(hotelId);

    }
}
