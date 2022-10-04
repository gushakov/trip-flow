package com.github.tripflow.core.usecase.home;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.WorkflowOperationsOutputPort;
import com.github.tripflow.core.port.presenter.home.WelcomePresenterOutputPort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
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

        String loggedInUsername;

        try {
            loggedInUsername = securityOps.loggedInUserName();
            securityOps.assertCustomerPermission(loggedInUsername);
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentWelcomeView(loggedInUsername, true);

    }

    /**
     * Start new trip booking workflow process and record corresponding
     * {@link Trip} aggregate instance in the database.
     */
    @Transactional
    @Override
    public void startNewTripBooking() {

        Long pik = null;
        String tripStartedBy;
        Trip trip;
        Optional<TripTask> nextTripTaskOpt;
        try {
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
            nextTripTaskOpt = dbOps.findAnyTasksForTripAndUser(tripId, candidateGroups, tripStartedBy)
                    .stream().findAny();

        } catch (GenericTripFlowError e) {

            // to be consistent, we need to cancel the created workflow
            // if there were problems creating, validating, or persisting
            // the corresponding aggregate
            if (pik != null) {
                workflowOps.cancelTripBookingProcess(pik);
            }

            presenter.presentError(e);
            return;
        }

        if (nextTripTaskOpt.isPresent()) {
            // go directly to the next active task for the user
            presenter.presentResultOfStartingNewTripBookingWithNextActiveTask(nextTripTaskOpt.get());
        } else {
            // it's OK, we'll present an (incomplete) list of tasks and let the user refresh
            presenter.presentResultOfStartingNewTripBookingWithoutNextActiveTask(trip.getTripId());
        }
    }
}
