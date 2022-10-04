package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.mapstruct.*;

import java.util.Map;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructJobTaskMapper implements JobTaskMapper {

    @Mapping(target = "candidateGroups", ignore = true)
    @Mapping(target = "tripStartedBy", ignore = true)
    @Mapping(target = "action", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "taskId", source = "key")
    @Mapping(target = "tripId", source = "processInstanceKey")
    protected abstract TripTask map(ActivatedJob job);

    @AfterMapping
    protected void mapJobVariables(ActivatedJob job, @MappingTarget TripTask.TripTaskBuilder tripTaskBuilder){
        // map job variables

        Map<String, Object> vars = job.getVariablesAsMap();
        tripTaskBuilder.action((String) vars.get(Constants.ACTION_VARIABLE));
        tripTaskBuilder.tripStartedBy((String) vars.get(Constants.TRIP_STARTED_BY_VARIABLE));
        tripTaskBuilder.name((String) vars.get(Constants.NAME_VARIABLE));
        tripTaskBuilder.candidateGroups(job.getCustomHeaders().get(Constants.CANDIDATE_GROUPS_HEADER));
    }

    @IgnoreForMapping
    @Override
    public TripTask convert(ActivatedJob job) {
        return map(job);
    }
}
