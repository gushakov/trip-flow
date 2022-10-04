package com.github.tripflow.core.usecase.hotel;

import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.trip.TripId;

public interface ReserveHotelInputPort {

    void proposeHotelsForSelectionByCustomer(Long taskId);

    void registerSelectedHotelWithTrip(String taskId, TripId tripId, HotelId hotelId);

    void confirmHotelReservation(Long taskId);
}
