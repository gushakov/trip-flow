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
        this.status = Optional.ofNullable(status).orElse(TripStatus.undefined);

        // these can be null
        this.flightNumber = flightNumber;
        this.hotelId = hotelId;
    }

    public boolean hasFlightBooked() {
        return flightNumber != null;
    }

    public Trip bookFlight(FlightNumber number) {
        return newTrip().flightNumber(number).build();
    }

    public Trip reserveHotel(HotelId hotelId) {

        // enforce aggregate invariant: we can only reserve hotel if
        // a flight was booked already for the trip

        if (!hasFlightBooked()){
            throw new TripFlowValidationError("Cannot reserve hotel for the trip before a flight has been booked.");
        }

        /*
            Discussion point:
            ----------------

            If we do not allow to reserve a hotel in the city different from the
            destination city of the flight, where should we enforce this rule?
            In the use case (reserveHotel)? Or in this method here? In the latter case,
            we would have to have the destination city as the attribute of "Trip"
            aggregate, and we would have to pass the city of the hotel as a parameter
            to this method.

         */

        return newTrip().hotelId(hotelId).build();
    }

    public boolean hasHotelReserved(){
        return hotelId != null;
    }

    private TripBuilder newTrip() {
        return new TripBuilder()
                .tripId(tripId)
                .startedBy(startedBy)
                .flightNumber(flightNumber)
                .hotelId(hotelId)
                ;

    }
}
