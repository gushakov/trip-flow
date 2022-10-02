package com.github.tripflow.infrastructure.adapter.web.home;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import com.github.tripflow.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.tripflow.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Presenters need to have "request" scope because we are autowiring {@code HttpServletRequest}
 * and {@code HttpServletResponse} objects associated with the thread processing current request.
 *
 * @see AbstractWebPresenter
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class HomePresenter extends AbstractWebPresenter implements WelcomePresenterOutputPort {
    public HomePresenter(@Qualifier("local") DispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super((LocalDispatcherServlet) dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentWelcomeView(String userName, boolean isCustomer) {

        presentModelAndView(Map.of("userName", userName,
                "isCustomer", isCustomer), "home");
    }

    @Override
    public void presentResultOfStartingNewTripBookingWithoutNextActiveTask(TripId tripId) {
        message("New process with ID: %s just started. You may need to refresh this view."
                .formatted(tripId.getShortId()));
        redirect("browseActiveTrips");
    }

    @Override
    public void presentResultOfStartingNewTripBookingWithNextActiveTask(TripTask tripTask) {
        message("New process with ID: %s just started. Next task: %s."
                .formatted(tripTask.getTripId().getShortId(), tripTask.getName()));
        redirect(tripTask.getAction(), Map.of("taskId", tripTask.getTaskId()));
    }
}
