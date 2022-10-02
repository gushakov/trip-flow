package com.github.tripflow.infrastructure.adapter.web.workflow;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.workflow.ContinueWorkflowPresenterOutputPort;
import com.github.tripflow.infrastructure.adapter.web.AbstractWebPresenter;
import com.github.tripflow.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
public class ContinueWorkflowPresenter extends AbstractWebPresenter implements ContinueWorkflowPresenterOutputPort {
    public ContinueWorkflowPresenter(LocalDispatcherServlet dispatcher, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        super(dispatcher, httpRequest, httpResponse);
    }

    @Override
    public void presentNextActiveTaskForUser(TripTask tripTask) {
        // TODO:
    }

    @Override
    public void presentNoActiveTasksFoundForUserResult(TripId tripId) {
        // TODO:
    }
}
