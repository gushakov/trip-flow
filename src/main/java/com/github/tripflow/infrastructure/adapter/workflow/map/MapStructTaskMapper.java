package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.task.TripFlowTask;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.tasklist.dto.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MapStructTaskMapper implements WorkflowTaskMapper {

    protected abstract TripFlowTask map(Task workflowTask);

    @IgnoreForMapping
    @Override
    public TripFlowTask convert(Task workflowTask) {
        return null;
    }
}
