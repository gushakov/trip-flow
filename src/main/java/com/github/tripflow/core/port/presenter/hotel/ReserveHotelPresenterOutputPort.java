package com.github.tripflow.core.port.presenter.hotel;

import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface ReserveHotelPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentHotelsInDestinationCityForSelectionByCustomer(Long taskId, Trip trip, String city, List<Hotel> hotels);

    void presentResultOfRegisteringSelectedHotelWithTrip(String taskId, TripId tripId, HotelId hotelId);

    void presentResultOfConfirmingHotelReservationWithoutNextActiveTask(TripId tripId);

    void presentResultOfConfirmingHotelReservationWithNextActiveTask(TripTask tripTask);
}
