package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.workflow.TaskNotFoundError;
import com.github.tripflow.core.port.operation.workflow.TaskOperationError;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.infrastructure.adapter.workflow.map.WorkflowTaskMapper;
import com.github.tripflow.infrastructure.config.TripFlowProperties;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SimpleAuthentication;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
    References:
    ----------

    1. Camunda, human tasks, best practices: https://docs.camunda.io/docs/components/best-practices/architecture/understanding-human-tasks-management/
 */


@Service
@Slf4j
public class ZeebeTaskListClientOperationsAdapter implements TasksOperationsOutputPort {

    private final TripFlowProperties tripFlowProps;

    private final CamundaTaskListClient taskListClient;
    private final WorkflowTaskMapper taskMapper;

    private final String taskListClientUserName;

    private final RetryTemplate retryTemplate;

    public ZeebeTaskListClientOperationsAdapter(TripFlowProperties tripFlowProps, WorkflowTaskMapper taskMapper,
                                                @Qualifier("taskListClient") RetryTemplate retryTemplate) {
        this.tripFlowProps = tripFlowProps;
        this.taskMapper = taskMapper;
        this.taskListClientUserName = tripFlowProps.getTaskListClientUserName();
        this.retryTemplate = retryTemplate;

        SimpleAuthentication auth = new SimpleAuthentication(tripFlowProps.getTaskListClientUserName(), tripFlowProps.getTaskListClientPassword());

        try {
            taskListClient = new CamundaTaskListClient.Builder()
                    .taskListUrl(UriComponentsBuilder.newInstance()
                            .scheme(tripFlowProps.getTaskListScheme())
                            .host(tripFlowProps.getTaskListHost())
                            .port(tripFlowProps.getTaskListPort())
                            .toUriString())
                    .authentication(auth)
                    .shouldReturnVariables()
                    .build();
        } catch (TaskListException e) {
            throw new TaskOperationError("Cannot instantiate client for operations with worflow tasks", e);
        }

    }

    @Override
    public List<TripTask> listActiveTasksForAssigneeCandidateGroup(String assigneeRole) {
        try {

            // Fetch the list of tasks with retry until all tasks are retrieved
            // with all variables. Note that we can only can get 1000 tasks
            // maximum at once.
            return retryTemplate.execute(retryContext -> taskListClient.getGroupTasks(assigneeRole,
                            TaskState.CREATED, 100, true)
                    .stream().map(taskMapper::convert).toList());

        } catch (TaskListException e) {
            throw new TaskOperationError(e.getMessage(), e);
        }
    }

    @Override
    public TripTask retrieveActiveTaskForAssigneeCandidateGroup(String taskId, String assigneeRole) {

        try {
            // fetch a task with matching task ID retrying if task variables are not available
            return retryTemplate.execute(retryContext ->
                    taskListClient.getGroupTasks(assigneeRole, TaskState.CREATED, 100, true)
                            .stream().filter(task -> task.getId().equals(taskId))
                            .findAny()
                            .map(taskMapper::convert)
                            .orElseThrow(() -> new TaskOperationError("Cannot find active task with ID: %s, assigned to candidate group: %s"
                                    .formatted(taskId, assigneeRole))));
        } catch (TaskListException e) {
            throw new TaskOperationError(e.getMessage(), e);
        }

    }

    @Override
    public void completeTask(String taskId) {
        try {

            // need to claim the task for "demo" (Camunda administrator) user used by TaskList client
            // before completing it on behalf of the current user of Trip Flow

            taskListClient.claim(taskId, taskListClientUserName);

            // complete the task
            taskListClient.completeTask(taskId, Map.of());
        } catch (TaskListException e) {
            throw new TaskOperationError(e.getMessage(), e);
        }
    }

    @Override
    public Optional<TripTask> retrieveNextActiveTaskForUser(TripId tripId, String assigneeRole, String tripStartedBy) {

        try {
            return retryTemplate.execute(retryContext -> {
                log.debug("[TaskList Client][Retry] Will try to fetch an active task for candidature group: {}, started by: {}," +
                        " retry count: {}", assigneeRole, tripStartedBy, retryContext.getRetryCount());

                Optional<TripTask> tripTaskOpt = taskListClient.getGroupTasks(assigneeRole, TaskState.CREATED, 100, true)
                        .stream()
                        .map(taskMapper::convert)
                        .filter(tripTask -> tripTask.getTripId().equals(tripId)
                                && tripTask.getTripStartedBy().equals(tripStartedBy))
                        .findAny();

                // retry if no task could be retrieved from the workflow engine,
                // and we have not yer exhausted the number of retries
                if (tripTaskOpt.isEmpty() && retryContext.getRetryCount() < tripFlowProps.getTaskListClientMaxRetries()) {
                    log.debug("[TaskList Client][Retry] Matching task was not found, will retry, retry count: {}",
                            retryContext.getRetryCount());
                    throw new TaskNotFoundError();
                }

                return tripTaskOpt;
            });


        } catch (TaskNotFoundError e) {
            return Optional.empty();
        } catch (TaskListException e) {
            throw new TaskOperationError(e.getMessage(), e);
        }

    }
}
