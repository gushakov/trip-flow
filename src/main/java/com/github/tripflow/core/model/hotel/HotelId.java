package com.github.tripflow.core.model.hotel;

import lombok.Value;

import static com.github.tripflow.core.model.Validator.notNull;

@Value
public class HotelId {

    String id;

    public static HotelId of(String id) {
        return new HotelId(id);
    }

    public HotelId(String id) {
        this.id = notNull(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
