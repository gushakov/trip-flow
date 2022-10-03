package com.github.tripflow.infrastructure.adapter.web.summary;

import com.github.tripflow.core.model.summary.TripSummary;
import com.github.tripflow.core.port.presenter.summary.ViewSummaryPresenterOutputPort;
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
}
