package com.github.tripflow.infrastructure.adapter.db.map;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripEntry;
import com.github.tripflow.infrastructure.adapter.db.flight.FlightEntity;
import com.github.tripflow.infrastructure.adapter.db.hotel.HotelEntity;
import com.github.tripflow.infrastructure.adapter.db.task.OpenTripQueryRow;
import com.github.tripflow.infrastructure.adapter.db.task.TripTaskEntity;
import com.github.tripflow.infrastructure.adapter.db.trip.TripEntity;

public interface TripFlowDbMapper {

    TripTaskEntity convert(TripTask tripTask);

    TripTask convert(TripTaskEntity tripTaskEntity);

    TripEntity convert(Trip trip);

    Trip convert(TripEntity tripEntity);

    TripEntry convert(OpenTripQueryRow row);

    Flight convert(FlightEntity flightEntity);

    Hotel convert(HotelEntity hotelEntity);

}
