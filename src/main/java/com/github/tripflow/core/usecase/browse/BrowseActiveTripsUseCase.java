package com.github.tripflow.core.usecase.browse;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.trip.TripEntry;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.presenter.browse.BrowseActiveTripsPresenterOutputPort;
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

            tripEntries = dbOps.findAnyTaskAssignedToCandidateGroupsAndWhereTripStartedByUser(candidateGroups,
                            loggedInUserName)
                    .stream()
                    .map(task -> TripEntry.builder()
                            .taskId(task.getTaskId())
                            .tripId(task.getTripId())
                            .taskName(task.getName())
                            .taskAction(task.getAction())
                            .flightBooked(false)
                            .hotelReserved(false)
                            .build())
                    .toList();

        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentActiveTasksForTripsStartedByUser(tripEntries);
    }
}
