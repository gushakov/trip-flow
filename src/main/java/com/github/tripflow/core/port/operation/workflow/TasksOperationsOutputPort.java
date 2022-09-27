package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.model.trip.TripTask;

import java.util.List;

public interface TasksOperationsOutputPort {

    List<TripTask> listActiveTasksForAssigneeCandidateGroup(String assigneeRole);

    TripTask retrieveActiveTaskForAssigneeCandidateGroup(String taskId, String assigneeRole);

    void completeTask(String taskId);
}
