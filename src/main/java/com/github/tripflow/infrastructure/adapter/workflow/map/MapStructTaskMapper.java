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

    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "active", source = "taskState")
    @Mapping(target = "tripStartedBy", source = "workflowTask", qualifiedByName = "mapTripStartedByFromTaskVariable")
    @Mapping(target = "action", source = "workflowTask", qualifiedByName = "mapActionFromTaskVariable")
    @Mapping(target = "tripId", source = "workflowTask", qualifiedByName = "mapTripIdFromTaskVariable")
    @Mapping(target = "flightBooked", source = "workflowTask", qualifiedByName = "mapFlightBookedFromTaskVariable")
    protected abstract TripTask map(Task workflowTask);

    @Named("mapTripIdFromTaskVariable")
    protected TripId mapTripIdFromTaskVariable(Task workflowTask) {
        Long tripId = workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.TRIP_ID_PROCESS_VARIABLE))
                .map(Variable::getValue)
                .map(Long.class::cast)
                .findFirst()
                .orElseThrow(() -> new TaskOperationError("No %s variable associated with the task: %s"
                        .formatted(Constants.TRIP_ID_PROCESS_VARIABLE, workflowTask.getId())));
        return TripId.of(tripId);
    }

    @Named("mapActionFromTaskVariable")
    protected String mapActionFromTaskVariable(Task workflowTask) {
        return workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.ACTION_VARIABLE))
                .map(Variable::getValue)
                .map(String.class::cast)
                .findFirst()
                .orElseThrow(() -> new TaskOperationError("No %s variable associated with the task: %s"
                        .formatted(Constants.ACTION_VARIABLE, workflowTask.getId())));
    }

    @Named("mapFlightBookedFromTaskVariable")
    protected boolean mapFlightBookedFromTaskVariable(Task workflowTask) {
        return workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.FLIGHT_BOOKED_VARIABLE))
                .map(Variable::getValue)
                .map(Boolean.class::cast)
                .findFirst()
                .orElse(false);
    }

    @Named("mapTripStartedByFromTaskVariable")
    protected String mapTripStartedByFromTaskVariable(Task workflowTask) {
        return workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.TRIP_STARTED_BY_VARIABLE))
                .map(Variable::getValue)
                .map(String.class::cast)
                .findFirst()
                .orElseThrow(() -> new TaskOperationError("No %s variable associated with the task: %s"
                        .formatted(Constants.TRIP_STARTED_BY_VARIABLE, workflowTask.getId())));
    }

    @IgnoreForMapping
    @Override
    public TripTask convert(Task workflowTask) {
        return map(workflowTask);
    }
}
