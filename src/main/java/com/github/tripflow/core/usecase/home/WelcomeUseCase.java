package com.github.tripflow.core.usecase.home;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class WelcomeUseCase implements WelcomeInputPort {

    private final WelcomePresenterOutputPort presenter;
    private final SecurityOperationsOutputPort securityOps;

    private final WorkflowOperationsOutputPort workflowOps;

    /**
     * Welcome user by presenting a list of available operations
     * depending on her role in TripFlow application. Uses security
     * operations to retrieve user's information (username, roles).
     */
    @Override
    public void welcomeUser() {

        String userName = securityOps.loggedInUserName();

        presenter.presentWelcomeView(userName);

    }

    /**
     * Start new trip booking workflow process and record corresponding
     * {@link Trip} aggregate instance in the database.
     */
    @Transactional
    @Override
    public void startNewTripBooking() {

    }
}
