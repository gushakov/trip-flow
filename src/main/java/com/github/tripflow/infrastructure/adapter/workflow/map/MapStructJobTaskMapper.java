package com.github.tripflow.infrastructure.adapter.workflow.map;

import com.github.tripflow.core.WorkflowJobToTaskMappingError;
import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.task.TripTask;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import com.github.tripflow.infrastructure.map.IgnoreForMapping;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.Map;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public abstract class MapStructJobTaskMapper implements JobTaskMapper {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "candidateGroups", ignore = true)
    @Mapping(target = "tripStartedBy", ignore = true)
    @Mapping(target = "action", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "taskId", source = "key")
    @Mapping(target = "tripId", source = "processInstanceKey")
    protected abstract TripTask map(ActivatedJob job, @Context boolean isUserTask);

    @AfterMapping
    protected void mapJobVariables(ActivatedJob job, @MappingTarget TripTask.TripTaskBuilder tripTaskBuilder,
                                   @Context boolean isUserTask) {

        Map<String, Object> vars = job.getVariablesAsMap();
        tripTaskBuilder.tripStartedBy((String) vars.get(Constants.TRIP_STARTED_BY_VARIABLE));
        if (isUserTask) {
            tripTaskBuilder.action((String) vars.get(Constants.ACTION_VARIABLE));
            tripTaskBuilder.candidateGroups(unwrapFirstCandidateGroup(job.getCustomHeaders().get(Constants.CANDIDATE_GROUPS_HEADER)));
            tripTaskBuilder.name((String) vars.getOrDefault(Constants.NAME_VARIABLE, job.getType()));
        } else {
            // these are not used for service tasks
            tripTaskBuilder.action("NA");
            tripTaskBuilder.candidateGroups("NA");
            tripTaskBuilder.name(job.getType());
        }

    }

    // Unwrap "[\"ROLE_TRIPFLOW_CUSTOMER,SOME_OTHER_ROLE\"]" into "ROLE_TRIPFLOW_CUSTOMER"
    private String unwrapFirstCandidateGroup(String candidateGroupsRaw) {
        return Arrays.stream(candidateGroupsRaw
                        .strip()
                        .replaceFirst("^\\[\"", "")
                        .replaceFirst("\"]$", "")
                        .split(","))
                .findFirst().orElseThrow(IllegalStateException::new);
    }

    @IgnoreForMapping
    @Override
    public TripTask convertUserTask(ActivatedJob job) {
        try {
            return map(job, true);
        } catch (Exception e) {
            throw new WorkflowJobToTaskMappingError("Error mapping job: %s to a trip task".formatted(job), e);
        }
    }

    @IgnoreForMapping
    @Override
    public TripTask convertServiceTask(ActivatedJob job) {
        try {
            return map(job, false);
        } catch (Exception e) {
            throw new WorkflowJobToTaskMappingError("Error mapping job: %s to a trip task".formatted(job), e);
        }
    }
}
