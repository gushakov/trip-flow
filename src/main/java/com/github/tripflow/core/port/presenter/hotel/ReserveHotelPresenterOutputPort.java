package com.github.tripflow.core.port.presenter.hotel;

import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.presenter.ErrorHandlingPresenterOutputPort;

import java.util.List;

public interface ReserveHotelPresenterOutputPort extends ErrorHandlingPresenterOutputPort {
    void presentHotelsInDestinationCityForSelectionByCustomer(String taskId, TripId tripId, String city, List<Hotel> hotels);
}
