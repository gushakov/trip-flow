package com.github.tripflow.core.model.flight;

import lombok.Value;

import static com.github.tripflow.core.model.Validator.notNull;

@Value
public class FlightNumber {

    String number;

    public static FlightNumber of(String number) {
        return new FlightNumber(number);
    }

    public FlightNumber(String number) {
        this.number = notNull(number);
    }

    @Override
    public String toString() {
        return number;
    }
}
