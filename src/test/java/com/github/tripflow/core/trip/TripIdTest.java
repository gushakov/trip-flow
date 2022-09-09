package com.github.tripflow.core.trip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TripIdTest {

    @Test
    void must_not_accept_null_id() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new TripId(null));
    }
}
