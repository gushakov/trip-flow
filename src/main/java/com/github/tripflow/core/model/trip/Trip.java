package com.github.tripflow.core.model.trip;

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

    @Builder
    public Trip(TripId tripId, String startedBy) {
        this.tripId = notNull(tripId);
        this.startedBy = notNull(startedBy);
    }

}
