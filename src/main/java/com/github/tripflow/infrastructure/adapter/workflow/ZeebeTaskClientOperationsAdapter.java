package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.model.task.TripFlowTask;
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
import java.util.stream.Collectors;

@Service
public class ZeebeTaskClientOperationsAdapter implements TasksOperationsOutputPort {

    private final CamundaTaskListClient taskListClient;
    private final WorkflowTaskMapper taskMapper;

    public ZeebeTaskClientOperationsAdapter(TripFlowProperties tripFlowProps, WorkflowTaskMapper taskMapper) {

        this.taskMapper = taskMapper;

        SimpleAuthentication auth = new SimpleAuthentication(tripFlowProps.getTaskClientUserName(), tripFlowProps.getTaskClientPassword());

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
    public List<TripFlowTask> listActiveTasksForAssignee(String assigneeRole) {
        try {
            return taskListClient.getAssigneeTasks(assigneeRole, TaskState.CREATED, Integer.MAX_VALUE, true)
                    .stream().map(taskMapper::convert).collect(Collectors.toList());
        } catch (TaskListException e) {
            throw new TaskOperationError(e.getMessage(), e);
        }
    }
}