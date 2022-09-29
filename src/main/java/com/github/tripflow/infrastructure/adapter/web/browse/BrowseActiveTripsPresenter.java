package com.github.tripflow.infrastructure.adapter.web.browse;

import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.presenter.browse.BrowseActiveTripsPresenterOutputPort;
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
public class BrowseActiveTripsPresenter extends AbstractWebPresenter implements BrowseActiveTripsPresenterOutputPort {
    public BrowseActiveTripsPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentActiveTasksForTripsStartedByUser(List<TripTask> tasks) {

        presentModelAndView(Map.of("tripTasks", tasks), "browse-active-trips");

    }
}
