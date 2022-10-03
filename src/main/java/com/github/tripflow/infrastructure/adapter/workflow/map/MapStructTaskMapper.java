package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.workflow.TaskNotFoundError;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.Variable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper which will convert an instance of a workflow {@linkplain Task}
 * into our {@linkplain TripTask} aggregate instance. This mapper will map certain
 * variables which are assumed to always be present in a scope of a user task.
 *
 * @see Task#getVariables()
 * @see TaskNotFoundError
 */
@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructTaskMapper implements WorkflowTaskMapper {

    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "tripStartedBy", source = "workflowTask", qualifiedByName = "mapTripStartedByFromTaskVariable")
    @Mapping(target = "action", source = "workflowTask", qualifiedByName = "mapActionFromTaskVariable")
    @Mapping(target = "tripId", source = "workflowTask", qualifiedByName = "mapTripIdFromTaskVariable")
    protected abstract TripTask map(Task workflowTask);

    @Named("mapTripIdFromTaskVariable")
    protected TripId mapTripIdFromTaskVariable(Task workflowTask) {
        return workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.TRIP_ID_PROCESS_VARIABLE))
                .map(Variable::getValue)
                .map(Long.class::cast)
                .findFirst()
                .map(TripId::of)
                .orElseThrow(TaskNotFoundError::new);
    }

    @Named("mapActionFromTaskVariable")
    protected String mapActionFromTaskVariable(Task workflowTask) {
        return workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.ACTION_VARIABLE))
                .map(Variable::getValue)
                .map(String.class::cast)
                .findFirst()
                .orElseThrow(TaskNotFoundError::new);
    }

    @Named("mapTripStartedByFromTaskVariable")
    protected String mapTripStartedByFromTaskVariable(Task workflowTask) {
        return workflowTask.getVariables().stream()
                .filter(variable -> variable.getName().equals(Constants.TRIP_STARTED_BY_VARIABLE))
                .map(Variable::getValue)
                .map(String.class::cast)
                .findFirst()
                .orElseThrow(TaskNotFoundError::new);
    }

    @IgnoreForMapping
    @Override
    public TripTask convert(Task workflowTask) {
        return map(workflowTask);
    }
}
