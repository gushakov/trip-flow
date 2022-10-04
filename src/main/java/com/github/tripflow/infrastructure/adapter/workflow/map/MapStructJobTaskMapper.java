package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructJobTaskMapper implements JobTaskMapper {

    @Mapping(target = "tripStartedBy", source = "job", qualifiedByName = "mapTripStartedByFromJobVariable")
    @Mapping(target = "action", ignore = true)
    @Mapping(target = "name", source = "type")
    @Mapping(target = "taskId", source = "elementInstanceKey")
    @Mapping(target = "tripId", source = "processInstanceKey")
    protected abstract TripTask map(ActivatedJob job);

    @Named("mapTripStartedByFromJobVariable")
    protected String mapTripStartedByFromJobVariable(ActivatedJob job) {
        return (String) job.getVariablesAsMap().get(Constants.TRIP_STARTED_BY_VARIABLE);
    }

    @IgnoreForMapping
    @Override
    public TripTask convert(ActivatedJob job) {
        return map(job);
    }
}
