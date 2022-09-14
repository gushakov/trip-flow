package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.tasklist.dto.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class MapStructTaskMapper implements WorkflowTaskMapper {

    @Mapping(target = "tripId", ignore = true)
    @Mapping(target = "taskId", source = "id")
    protected abstract TripTask map(Task workflowTask);

    @IgnoreForMapping
    @Override
    public TripTask convert(Task workflowTask) {
        return null;
    }
}
