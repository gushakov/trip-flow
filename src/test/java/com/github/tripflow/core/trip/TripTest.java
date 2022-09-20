package com.github.tripflow.core.trip;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TripTest {

    @Test
    void must_be_equal_if_equal_trip_id() {

        Trip tripOne = Trip.builder()
                .tripId(TripId.of(12345))
                .startedBy("user1")
                .build();
        Trip tripTwo = Trip.builder()
                .tripId(TripId.of(12345))
                .startedBy("user2")
                .build();

        assertThat(tripOne.equals(tripTwo)).isTrue();
    }
}
