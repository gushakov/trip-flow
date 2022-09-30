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

    public boolean hasFlightSelected() {
        return flightNumber != null;
    }

    public Trip bookFlight(FlightNumber number) {
        return newTrip().flightNumber(number).build();
    }

    public Trip reserveHotel(HotelId hotelId) {
        return newTrip().hotelId(hotelId).build();
    }

    public boolean hasHotelSelected(){
        return hotelId != null;
    }

    private TripBuilder newTrip() {
        return new TripBuilder()
                .tripId(tripId)
                .startedBy(startedBy)
                .flightNumber(flightNumber)
                .hotelId(hotelId);

    }
}
