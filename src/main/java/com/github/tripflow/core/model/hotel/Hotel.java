package com.github.tripflow.core.model.hotel;

import com.github.tripflow.core.model.Validator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static com.github.tripflow.core.model.Validator.notNull;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Hotel {

    @EqualsAndHashCode.Include
    HotelId hotelId;

    String name;

    String city;

    int price;

    String imageFile;

    @Builder
    public Hotel(HotelId hotelId, String name, String city, int price, String imageFile) {
        this.hotelId = notNull(hotelId);
        this.name = notNull(name);
        this.city = notNull(city);
        this.price = notNull(price);
        this.imageFile = notNull(imageFile);
    }
}
