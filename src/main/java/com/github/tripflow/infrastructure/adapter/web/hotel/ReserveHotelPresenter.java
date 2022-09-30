package com.github.tripflow.infrastructure.adapter.web.hotel;

import com.github.tripflow.core.model.hotel.Hotel;
import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.presenter.hotel.ReserveHotelPresenterOutputPort;
import com.github.tripflow.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.tripflow.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class ReserveHotelPresenter extends AbstractWebPresenter implements ReserveHotelPresenterOutputPort {
    public ReserveHotelPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentHotelsInDestinationCityForSelectionByCustomer(String taskId, Trip trip, String city, List<Hotel> hotels) {

        HotelId hotelId = trip.getHotelId();
        presentModelAndView(Map.of("reserveHotelForm",
                ReserveHotelForm.builder()
                        .taskId(taskId)
                        .tripId(trip.getTripId().getId())
                        .selectedHotelId(hotelId != null ? hotelId.getId() : null)
                        .city(city)
                        .hotels(hotels)
                        .build()), "reserve-hotel");
    }

    @Override
    public void presentResultOfRegisteringSelectedHotelWithTrip(String taskId, TripId tripId, HotelId hotelId) {
        message("Successfully registered hotel %s for trip with ID: %s"
                .formatted(hotelId, tripId.getShortId()));
        redirect("reserveHotel", Map.of("taskId", taskId));
    }
}
