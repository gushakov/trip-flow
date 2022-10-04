package com.github.tripflow.infrastructure.adapter.workflow;

import com.github.tripflow.core.usecase.task.RegisterUserTaskInputPort;
import com.github.tripflow.infrastructure.adapter.workflow.map.JobTaskMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZeebeJobHandlingAdapter {

    private final ApplicationContext applicationContext;

    private final JobTaskMapper jobTaskMapper;

    @ZeebeWorker(type = "io.camunda.zeebe:userTask", forceFetchAllVariables = true)
    public void handleUserTask(final JobClient jobClient, final ActivatedJob job) {
        useCase().registerActivatedUserTask(jobTaskMapper.convert(job));
    }

    private RegisterUserTaskInputPort useCase() {
        return applicationContext.getBean(RegisterUserTaskInputPort.class);
    }

}
