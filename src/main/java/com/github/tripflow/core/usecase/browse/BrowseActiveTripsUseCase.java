package com.github.tripflow.core.usecase.browse;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.IllegalTripFlowStateError;
import com.github.tripflow.core.model.trip.*;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.browse.BrowseActiveTripsPresenterOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BrowseActiveTripsUseCase implements BrowseActiveTripsInputPort {

    private final BrowseActiveTripsPresenterOutputPort presenter;

    private final SecurityOperationsOutputPort securityOps;

    private final TasksOperationsOutputPort tasksOps;

    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void listActiveTasksForTripsStartedByUser() {

        List<TripEntry> tripEntries;
        try {
            // get all active tasks for trips started by the user
            String loggedInUserName = securityOps.loggedInUserName();
            Map<TripId, TripTask> tasks = tasksOps.listActiveTasksForAssigneeCandidateGroup(securityOps.tripFlowAssigneeRole())
                    .stream()
                    .filter(tripTask -> tripTask.getTripStartedBy().equals(loggedInUserName))
                    .collect(Collectors.toUnmodifiableMap(TripTask::getTripId, Function.identity()));

            // create a list of entries with information about trips to be presented
            tripEntries = dbOps.findOpenTripsStartedByUser(loggedInUserName)
                    .stream()
                    .filter(trip -> tasks.containsKey(trip.getTripId()))
                    .map(trip -> {
                        TripTask task = tasks.get(trip.getTripId());
                        return TripEntry.builder()
                                .taskId(task.getTaskId())
                                .tripId(trip.getTripId())
                                .taskName(task.getName())
                                .taskAction(task.getAction())
                                .flightBooked(trip.isFlightBooked())
                                .hotelReserved(trip.isHotelReserved())
                                .build();

                    })
                    .toList();

        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentActiveTasksForTripsStartedByUser(tripEntries);
    }
}
