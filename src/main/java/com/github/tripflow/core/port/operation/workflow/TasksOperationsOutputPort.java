package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.model.trip.TripTask;

import java.util.List;

public interface TasksOperationsOutputPort {

        List<TripTask> listActiveTasksForAssignee(String assigneeRole);
}
