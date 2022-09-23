package com.github.tripflow.infrastructure.adapter.web.flight;

import com.github.tripflow.core.model.flight.Flight;
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
    public void presentFlightsForSelectionByCustomer(TripId tripId, List<Flight> flights) {
        presentModelAndView(Map.of("tripId", tripId), "book-flight");
    }
}
