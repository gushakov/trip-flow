package com.github.tripflow.core.usecase.browse;

import com.github.tripflow.core.model.trip.TripEntry;
import com.github.tripflow.core.port.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.security.SecurityOperationsOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BrowseActiveTripsUseCase implements BrowseActiveTripsInputPort {

    private final BrowseActiveTripsPresenterOutputPort presenter;

    private final SecurityOperationsOutputPort securityOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void listEntriesForActiveTripsStartedByUser() {

        List<TripEntry> tripEntries;
        try {
            // get all active tasks for trips started by the user
            String loggedInUserName = securityOps.loggedInUserName();
            securityOps.assertCustomerPermission(loggedInUserName);

            String candidateGroups = securityOps.tripFlowAssigneeRole();

            tripEntries = dbOps.findAllOpenTripsForUser(candidateGroups, loggedInUserName);

        } catch (Exception e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentActiveTasksForTripsStartedByUser(tripEntries);
    }
}
