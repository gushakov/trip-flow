package com.github.tripflow.core.trip;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TripTest {

    @Test
    void must_be_equal_if_equal_trip_id() {

        Trip tripOne = Trip.builder().tripId(TripId.of(12345)).build();
        Trip tripTwo = Trip.builder().tripId(TripId.of(12345)).build();

        assertThat(tripOne.equals(tripTwo)).isTrue();
    }
}
