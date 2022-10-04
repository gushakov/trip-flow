package com.github.tripflow.infrastructure.adapter.web.hotel;

import com.github.tripflow.core.model.hotel.Hotel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReserveHotelForm {

    Long taskId;
    Long tripId;
    String city;
    List<Hotel> hotels;

    String selectedHotelId;

    public boolean isHotelSelected() {
        return selectedHotelId != null;
    }

}
