package com.github.tripflow.infrastructure.adapter.db.map;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.infrastructure.adapter.db.FlightEntity;
import com.github.tripflow.infrastructure.adapter.db.TripEntity;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructTripDbMapper implements TripDbMapper {

    protected abstract TripEntity map(Trip trip);

    protected abstract Trip map(TripEntity tripEntity);

    protected abstract Flight map(FlightEntity flightEntity);

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
}
