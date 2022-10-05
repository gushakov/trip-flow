package com.github.tripflow.infrastructure.adapter.web.outcome;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.presenter.outcome.ViewOutcomePresenterOutputPort;
import com.github.tripflow.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.tripflow.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class ViewOutcomePresenter extends AbstractWebPresenter implements ViewOutcomePresenterOutputPort {
    public ViewOutcomePresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentSuccessfulTripConfirmation(Long taskId, TripId tripId, String city) {
        ViewOutcomeForm form = ViewOutcomeForm.builder()
                .taskId(taskId)
                .tripId(tripId.getShortId())
                .success(true)
                .city(city)
                .build();

        presentModelAndView(Map.of("viewOutcomeForm", form),
                "view-outcome");
    }

    @Override
    public void presentRefusedTripBooking(Long taskId, TripId tripId, String city) {
        ViewOutcomeForm form = ViewOutcomeForm.builder()
                .taskId(taskId)
                .tripId(tripId.getShortId())
                .success(false)
                .city(city)
                .build();

        presentModelAndView(Map.of("viewOutcomeForm", form),
                "view-outcome");
    }

    @Override
    public void presentResultOfSuccessfullyFinishingProcess(TripId tripId) {
        message("Process for trip booking with ID: %s has finished. You may need to refresh this view.");
        redirect("browseActiveTrips");
    }
}
