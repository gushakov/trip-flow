package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.workflow.TaskOperationError;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.Variable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class MapStructTaskMapper implements WorkflowTaskMapper {

    @Mapping(target = "tripId", source = "workflowTask", qualifiedByName = "mapTripTaskIdFromTaskVariable")
    @Mapping(target = "taskId", source = "id")
    protected abstract TripTask map(Task workflowTask);

    @Named("mapTripTaskIdFromTaskVariable")
    protected TripId mapTripTaskIdFromTaskVariable(Task workflowTask) {
        Long tripId = workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals("tripId"))
                .map(Variable::getValue)
                .map(Long.class::cast)
                .findFirst().orElseThrow(() -> new TaskOperationError("No Trip ID variable associated with the task: %s"
                        .formatted(workflowTask.getId())));
        return TripId.of(tripId);
    }

    @IgnoreForMapping
    @Override
    public TripTask convert(Task workflowTask) {
        return map(workflowTask);
    }
}
