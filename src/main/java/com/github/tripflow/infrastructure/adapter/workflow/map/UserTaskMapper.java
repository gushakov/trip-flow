package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.task.TripTask;
import io.camunda.tasklist.dto.Task;

public interface UserTaskMapper {

    TripTask convert(Task workflowTask);

}
