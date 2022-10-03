package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.TripId;

import java.util.List;
import java.util.Optional;

public interface TasksOperationsOutputPort {

    List<TripTask> listActiveTasksForAssigneeCandidateGroup(String assigneeRole);

    TripTask retrieveActiveTaskForAssigneeCandidateGroup(String taskId, String assigneeRole);

    void completeTask(String taskId);

    Optional<TripTask> retrieveNextActiveTaskForUser(TripId tripId, String assigneeRole, String tripStartedBy);
}
