package com.github.tripflow.core.usecase.outcome;

public interface ViewOutcomeInputPort {
    void viewOutcome(Long taskId);

    void finishProcess(Long taskId);
}
