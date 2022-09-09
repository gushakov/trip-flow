package com.github.tripflow.core.trip;

import lombok.Value;

import static com.github.tripflow.core.Validator.notNull;

@Value
public class TripId {

    Long id;

    public static TripId of(long id){
        return new TripId(id);
    }

    public TripId(Long id) {
        this.id = notNull(id);
    }
}
