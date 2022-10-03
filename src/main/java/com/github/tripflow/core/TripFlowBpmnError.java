package com.github.tripflow.core;

import lombok.Getter;

/**
 * An error modeled in the BPMN process.
 */
public class TripFlowBpmnError extends GenericTripFlowError {
    @Getter
    private final String bpmnCode;

    public TripFlowBpmnError(String bpmnCode, String message) {
        super(message);
        this.bpmnCode = bpmnCode;
    }

}
