package com.github.tripflow.infrastructure.adapter.web.flight;

import com.github.tripflow.core.model.flight.Flight;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookFlightForm {

    Long taskId;
    Long tripId;
    String shortTripId;
    List<Flight> flights;
    String selectedFlightNumber;

    public boolean isFlightSelected() {
        return selectedFlightNumber != null;
    }
}
