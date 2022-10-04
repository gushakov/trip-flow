package com.github.tripflow.core.usecase.task;

import com.github.tripflow.core.model.task.TripTask;

public interface RegisterUserTaskInputPort {

    void registerActivatedUserTask(TripTask userTask);

}
