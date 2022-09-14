package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.trip.TripTask;
import io.camunda.tasklist.dto.Task;

public interface WorkflowTaskMapper {

    TripTask convert(Task workflowTask);

}
