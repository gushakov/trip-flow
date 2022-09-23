package com.github.tripflow.core.model.trip;

import com.github.tripflow.core.model.flight.FlightNumber;
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

    @Builder
    public Trip(TripId tripId, String startedBy, FlightNumber flightNumber) {
        this.tripId = notNull(tripId);
        this.startedBy = notNull(startedBy);
        this.flightNumber = flightNumber;
    }

    public Trip withFlightNumber(FlightNumber number) {
        return newTrip().flightNumber(number).build();
    }

    private TripBuilder newTrip(){
        return new TripBuilder()
                .tripId(tripId)
                .startedBy(startedBy)
                .flightNumber(flightNumber);

    }
}
