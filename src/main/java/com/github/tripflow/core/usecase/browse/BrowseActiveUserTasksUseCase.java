package com.github.tripflow.core.usecase.browse;

import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BrowseActiveUserTasksUseCase implements BrowseActiveUserTasksInputPort{

    private final BrowseActiveUserTasksInputPort presenter;

    private final TasksOperationsOutputPort tasksOps;

    @Override
    public void browseTasks() {



    }
}
