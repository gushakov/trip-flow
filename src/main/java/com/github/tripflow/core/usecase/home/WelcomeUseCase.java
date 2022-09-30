package com.github.tripflow.core.usecase.home;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.TripFlowValidationError;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.db.TripFlowDbPersistenceError;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.TripFlowSecurityError;
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

        String userName;
        boolean isCustomer;

        try {
            userName = securityOps.loggedInUserName();
            isCustomer = securityOps.isCustomer();
        }
        catch (GenericTripFlowError e){
            presenter.presentError(e);
            return;
        }

        presenter.presentWelcomeView(userName, isCustomer);

    }

    /**
     * Start new trip booking workflow process and record corresponding
     * {@link Trip} aggregate instance in the database.
     */
    @Transactional
    @Override
    public void startNewTripBooking() {

        Long pik;
        String tripStartedBy;
        Trip trip;

        // get the current user

        try {
            tripStartedBy = securityOps.loggedInUserName();
        } catch (TripFlowSecurityError e) {
            presenter.presentError(e);
            return;
        }

        // start a new instance of the TripFlow process

        try {
            pik = workflowOps.startNewTripBookingProcess(tripStartedBy);
        } catch (WorkflowClientOperationError e) {
            presenter.presentErrorStartingNewWorkflowInstance(e);
            return;
        }

        // if the process has started OK, proceed to create and persist a new instance of Trip
        // aggregate

        try {
            trip = Trip.builder()
                    .tripId(TripId.of(pik))
                    .startedBy(tripStartedBy)
                    .build();
            dbOps.saveNewTrip(trip);
        } catch (TripFlowValidationError | TripFlowDbPersistenceError e) {

            // to be consistent, we need to cancel the created workflow
            // if there were problems creating, validating, or persisting
            // the corresponding aggregate
            workflowOps.cancelTripBookingProcess(pik);
            presenter.presentError(e);
            return;
        }

        presenter.presentResultOfStartingNewTripBooking(trip.getTripId());
    }
}
