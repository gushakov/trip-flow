package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.task.TripFlowTask;
import io.camunda.tasklist.dto.Task;

public interface WorkflowTaskMapper {

    TripFlowTask convert(Task workflowTask);

}
