package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.task.TripTask;
import io.camunda.zeebe.client.api.response.ActivatedJob;

public interface JobTaskMapper {

    TripTask convertUserTask(ActivatedJob job);

    TripTask convertServiceTask(ActivatedJob job);

}
