package com.github.tripflow.infrastructure.adapter.db.map;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.infrastructure.adapter.db.flight.FlightEntity;
import com.github.tripflow.infrastructure.adapter.db.hotel.HotelEntity;
import com.github.tripflow.infrastructure.adapter.db.trip.TripEntity;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructTripFlowDbMapper implements TripFlowDbMapper {

    protected abstract TripEntity map(Trip trip);

    protected abstract Trip map(TripEntity tripEntity);

    protected abstract Flight map(FlightEntity flightEntity);

    protected abstract Hotel map(HotelEntity hotelEntity);

    @IgnoreForMapping
    @Override
    public TripEntity convert(Trip trip) {
        return map(trip);
    }

    @IgnoreForMapping
    @Override
    public Trip convert(TripEntity tripEntity) {
        return map(tripEntity);
    }

    @IgnoreForMapping
    @Override
    public Flight convert(FlightEntity flightEntity) {
        return map(flightEntity);
    }

    @IgnoreForMapping
    @Override
    public Hotel convert(HotelEntity hotelEntity) {
        return map(hotelEntity);
    }
}
