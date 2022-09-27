package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.model.trip.TripTask;
import com.github.tripflow.core.port.operation.workflow.TaskOperationError;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.infrastructure.adapter.workflow.map.WorkflowTaskMapper;
import com.github.tripflow.infrastructure.config.TripFlowProperties;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SimpleAuthentication;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

/*
    References:
    ----------

    1. Camunda, human tasks, best practices: https://docs.camunda.io/docs/components/best-practices/architecture/understanding-human-tasks-management/
 */


@Service
public class ZeebeTaskClientOperationsAdapter implements TasksOperationsOutputPort {

    private final CamundaTaskListClient taskListClient;
    private final WorkflowTaskMapper taskMapper;

    private final String taskListClientUserName;

    public ZeebeTaskClientOperationsAdapter(TripFlowProperties tripFlowProps, WorkflowTaskMapper taskMapper) {
        this.taskMapper = taskMapper;
        this.taskListClientUserName = tripFlowProps.getTaskListClientUserName();

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
            // can only get 1000 tasks at one time maximum
            return taskListClient.getGroupTasks(assigneeRole, TaskState.CREATED, 100, true)
                    .stream().map(taskMapper::convert).toList();
        } catch (TaskListException e) {
            throw new TaskOperationError(e.getMessage(), e);
        }
    }

    @Override
    public TripTask retrieveActiveTaskForAssigneeCandidateGroup(String taskId, String assigneeRole) {

        try {
            return taskMapper.convert(taskListClient.getGroupTasks(assigneeRole, TaskState.CREATED, 100, true)
                    .stream().filter(task -> task.getId().equals(taskId))
                    .findAny()
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

            taskMapper.convert(taskListClient.completeTask(taskId, Map.of(Constants.FLIGHT_BOOKED_VARIABLE, true)));
        } catch (TaskListException e) {
            throw new TaskOperationError(e.getMessage(), e);
        }

    }
}
