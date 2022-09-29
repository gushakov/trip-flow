package com.github.tripflow.infrastructure.adapter.db.map;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.infrastructure.adapter.db.FlightEntity;
import com.github.tripflow.infrastructure.adapter.db.HotelEntity;
import com.github.tripflow.infrastructure.adapter.db.TripEntity;

public interface TripFlowDbMapper {

    TripEntity convert(Trip trip);

    Trip convert(TripEntity tripEntity);

    Flight convert(FlightEntity flightEntity);

    Hotel convert(HotelEntity hotelEntity);

}
