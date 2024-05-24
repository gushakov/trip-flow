package com.github.tripflow.core.usecase.home;

import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.workflow.WorkflowClientOperationError;
import com.github.tripflow.core.port.workflow.WorkflowOperationsOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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
        try {
            String loggedInUsername = securityOps.loggedInUserName();
            securityOps.assertCustomerPermission(loggedInUsername);
            presenter.presentWelcomeView(loggedInUsername, true);

        } catch (Exception e) {
            presenter.presentError(e);
        }
    }

    /**
     * Start new trip booking workflow process and record corresponding
     * {@link Trip} aggregate instance in the database.
     */
    @Override
    public void startNewTripBooking() {

        Long pik = null;
        try {
            String tripStartedBy;
            Trip trip;
            Optional<TripTask> nextTripTaskOpt;

            // get the current user's username and TripFlow assignee role

            tripStartedBy = securityOps.loggedInUserName();
            securityOps.assertCustomerPermission(tripStartedBy);

            // start a new instance of the TripFlow process

            pik = workflowOps.startNewTripBookingProcess(tripStartedBy);

            // if the process has started OK, proceed to create and persist a new instance of Trip
            // aggregate

            TripId tripId = TripId.of(pik);
            trip = Trip.builder()
                    .tripId(tripId)
                    .startedBy(tripStartedBy)
                    .build();
            dbOps.saveNewTrip(trip);

            String candidateGroups = securityOps.tripFlowAssigneeRole();

            // wait for the workflow engine to activate some user task
            nextTripTaskOpt = dbOps.findTasksForTripAndUserWithRetry(tripId, candidateGroups, tripStartedBy)
                    .stream().findAny();

            if (nextTripTaskOpt.isPresent()) {
                // go directly to the next active task for the user
                presenter.presentResultOfStartingNewTripBookingWithNextActiveTask(nextTripTaskOpt.get());
            } else {
                // it's OK, we'll present an (incomplete) list of tasks and let the user refresh
                presenter.presentResultOfStartingNewTripBookingWithoutNextActiveTask(trip.getTripId());
            }

        } catch (WorkflowClientOperationError e) {

            // to be consistent, we need to cancel the created workflow
            // if there were problems creating, validating, or persisting
            // the corresponding aggregate
            if (pik != null) {
                workflowOps.cancelTripBookingProcess(pik);
            }

            presenter.presentError(e);
        } catch (Exception e) {
            // for any other errors
            presenter.presentError(e);
        }

    }
}
