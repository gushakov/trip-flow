package com.github.tripflow.infrastructure.adapter.web.summary;

import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripSummary;
import com.github.tripflow.core.usecase.summary.ViewSummaryPresenterOutputPort;
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
public class ViewSummaryPresenter extends AbstractWebPresenter implements ViewSummaryPresenterOutputPort {
    public ViewSummaryPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentTripSummary(TripSummary tripSummary) {
        presentModelAndView(Map.of("tripSummary", tripSummary), "view-summary");
    }

    @Override
    public void presentResultOfProceedingWithPaymentWithoutNextActiveTask(TripId tripId) {
        message("Request for confirmation of the trip %s has been submitted. You may need to refresh this view."
                .formatted(tripId.getShortId()));
        redirect("browseActiveTrips");
    }

    @Override
    public void presentResultOfProceedingWithPaymentWithNextActiveTask(TripTask tripTask) {
        message("Request for confirmation of the trip %s has been processed. Next task: %s."
                .formatted(tripTask.getTripId().getShortId(), tripTask.getName()));
        redirect(tripTask.getAction(), Map.of("taskId", Long.toString(tripTask.getTaskId())));
    }
}
