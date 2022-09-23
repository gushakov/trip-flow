package com.github.tripflow.infrastructure.map;

import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.TripId;
import io.camunda.tasklist.dto.TaskState;
import org.mapstruct.Mapper;

/*
    References:
    ----------

    1.  Reusable MapStruct converters: https://github.com/gushakov/cargo-clean/blob/main/src/main/java/com/github/cargoclean/infrastructure/adapter/map/CommonMapStructConverters.java
 */

/**
 * MapStruct converter for shared objects. Specifies conversion between
 * Value Objects and their serialized representation used for persistence,
 * for example.
 */
@Mapper(componentModel = "spring")
public class CommonMapStructConverters {

    public Long convertTripIdToLong(TripId tripId) {
        return tripId.getId();
    }

    public TripId convertLongToTripId(Long id) {
        return TripId.of(id);
    }

    public FlightNumber convertStringToFlightNumber(String number){
        return FlightNumber.of(number);
    }

    public String convertFlightNumberToString(FlightNumber flightNumber){
        return flightNumber.getNumber();
    }

    public Boolean convertTaskStateToBoolean(TaskState taskState){
        return taskState == TaskState.CREATED;
    }

}
