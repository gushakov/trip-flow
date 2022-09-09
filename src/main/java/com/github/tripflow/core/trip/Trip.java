package com.github.tripflow.core.trip;

import com.github.tripflow.core.Validator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.github.tripflow.core.Validator.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trip {

    @EqualsAndHashCode.Include
    TripId tripId;

    @Builder
    public Trip(TripId tripId) {
        this.tripId = notNull(tripId);
    }

}
