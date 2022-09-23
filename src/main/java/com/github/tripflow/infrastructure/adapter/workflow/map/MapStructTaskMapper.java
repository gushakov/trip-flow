package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.workflow.TaskOperationError;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.Variable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructTaskMapper implements WorkflowTaskMapper {

    @Mapping(target = "assigneeRole", source = "assignee")
    @Mapping(target = "active", source = "taskState")
    @Mapping(target = "action", source = "workflowTask", qualifiedByName = "mapActionFromTaskVariable")
    @Mapping(target = "tripId", source = "workflowTask", qualifiedByName = "mapTripIdFromTaskVariable")
    @Mapping(target = "taskId", source = "id")
    protected abstract TripTask map(Task workflowTask);

    @Named("mapTripIdFromTaskVariable")
    protected TripId mapTripIdFromTaskVariable(Task workflowTask) {
        Long tripId = workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.TRIP_ID_PROCESS_VARIABLE))
                .map(Variable::getValue)
                .map(Long.class::cast)
                .findFirst()
                .orElseThrow(() -> new TaskOperationError("No Trip ID variable associated with the task: %s"
                        .formatted(workflowTask.getId())));
        return TripId.of(tripId);
    }

    @Named("mapActionFromTaskVariable")
    protected String mapActionFromTaskVariable(Task workflowTask) {
        return workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.ACTION_VARIABLE))
                .map(Variable::getValue)
                .map(String.class::cast)
                .findFirst()
                .orElseThrow(() -> new TaskOperationError("No action variable associated with the task: %s"
                        .formatted(workflowTask.getId())));
    }

    @IgnoreForMapping
    @Override
    public TripTask convert(Task workflowTask) {
        return map(workflowTask);
    }
}
