package com.github.tripflow.infrastructure.adapter.web.flight;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.presenter.flight.BookFlightPresenterOutputPort;
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
public class BookFlightPresenter extends AbstractWebPresenter implements BookFlightPresenterOutputPort {
    public BookFlightPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentFlightsForSelectionByCustomer(Trip trip, List<Flight> flights) {
        FlightNumber flightNumber = trip.getFlightNumber();
        presentModelAndView(Map.of("bookFlightForm",
                BookFlightForm.builder()
                        .tripId(trip.getTripId().getId())
                        .flights(flights)
                        .selectedFlightNumber(flightNumber != null ? flightNumber.getNumber() : null)
                        .build()), "book-flight");
    }

    @Override
    public void presentResultOfRegisteringSelectedFlightWithTrip(TripId tripId, FlightNumber flightNumber) {
        message("Successfully registered flight %s for trip with ID: %s"
                .formatted(tripId, flightNumber));
        redirect("bookFlight", Map.of("tripId", tripId.toString()));
    }
}
