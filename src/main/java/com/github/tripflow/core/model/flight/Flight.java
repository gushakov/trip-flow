package com.github.tripflow.core.model.flight;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.github.tripflow.core.model.Validator.notNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Flight {

    @EqualsAndHashCode.Include
    private final FlightNumber flightNumber;

    private final String airline;

    private final String originCity;

    private final String originIataCode;

    private final String destinationCity;

    private final String destinationIataCode;

    private final int price;

    @Builder
    public Flight(FlightNumber flightNumber, String airline, String originCity, String originIataCode,
                  String destinationCity, String destinationIataCode, int price) {
        this.flightNumber = notNull(flightNumber);
        this.airline = notNull(airline);
        this.originCity = notNull(originCity);
        this.originIataCode = notNull(originIataCode);
        this.destinationCity = notNull(destinationCity);
        this.destinationIataCode = notNull(destinationIataCode);
        this.price = price;
    }
}
