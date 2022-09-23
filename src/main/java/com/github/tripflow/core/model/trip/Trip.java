package com.github.tripflow.core.model.trip;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.github.tripflow.core.model.Validator.notNull;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trip {

    @EqualsAndHashCode.Include
    private final TripId tripId;

    private final String startedBy;

    @Builder
    public Trip(TripId tripId, String startedBy) {
        this.tripId = notNull(tripId);
        this.startedBy = notNull(startedBy);
    }

}
