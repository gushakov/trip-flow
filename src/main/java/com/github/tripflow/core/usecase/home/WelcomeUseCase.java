package com.github.tripflow.core.usecase.home;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.db.TripFlowDbPersistenceError;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.WorkflowClientOperationError;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class WelcomeUseCase implements WelcomeInputPort {

    private final WelcomePresenterOutputPort presenter;
    private final SecurityOperationsOutputPort securityOps;

    private final WorkflowOperationsOutputPort workflowOps;

    private final DbPersistenceOperationsOutputPort dbOps;

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

        // first we need to start a new instance of the TripFlow process

        Long pik;
        try {
            try {
                pik = workflowOps.startNewTripBookingProcess();
            } catch (WorkflowClientOperationError e) {
                presenter.presentErrorStartingNewWorkflowInstance(e);
                return;
            }

            // if the process started OK, proceed to create new instance of Trip
            // aggregate

            Trip trip = Trip.builder()
                        .tripId(TripId.of(pik))
                        .build();

            // try to persist Trip instance to DB
            try {
                dbOps.save(trip);
            } catch (TripFlowDbPersistenceError e) {

                // to be consistent we need to cancel the created workflow
                workflowOps.cancelTripBookingProcess(pik);
            }
        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfStartingNewTripBooking(pik);
    }
}
