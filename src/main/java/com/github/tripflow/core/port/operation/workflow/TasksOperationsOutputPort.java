package com.github.tripflow.core.port.operation.workflow;

import com.github.tripflow.core.model.task.TripFlowTask;

import java.util.List;

public interface TasksOperationsOutputPort {

        List<TripFlowTask> listActiveTasksForAssignee(String assigneeRole);
}
