package com.github.tripflow.core.model.trip;

import lombok.Value;

import static com.github.tripflow.core.model.Validator.notNull;

@Value
public class TripId {

    Long id;

    public static TripId of(long id) {
        return new TripId(id);
    }

    public TripId(Long id) {
        this.id = notNull(id);
    }

    public String getShortId(){
        return "T" + toString().substring(12);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
