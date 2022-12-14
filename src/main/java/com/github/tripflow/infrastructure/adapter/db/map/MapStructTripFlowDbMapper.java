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
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructTripFlowDbMapper implements TripFlowDbMapper {

    protected abstract TripTaskEntity map(TripTask tripTask);

    protected abstract TripTask map(TripTaskEntity tripTaskEntity);

    protected abstract TripEntity map(Trip trip);

    protected abstract Trip map(TripEntity tripEntity);

    protected abstract TripEntry map(OpenTripQueryRow row);

    protected abstract Flight map(FlightEntity flightEntity);

    protected abstract Hotel map(HotelEntity hotelEntity);

    @IgnoreForMapping
    @Override
    public TripTaskEntity convert(TripTask tripTask) {
        return map(tripTask);
    }

    @IgnoreForMapping
    @Override
    public TripTask convert(TripTaskEntity tripTaskEntity) {
        return map(tripTaskEntity);
    }

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
    public TripEntry convert(OpenTripQueryRow row) {
        return map(row);
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
