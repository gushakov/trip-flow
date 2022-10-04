package com.github.tripflow.core.usecase.confirmation;

public interface ConfirmTripInputPort {
    void confirmTrip(Long taskId);

    void refuseTrip(Long taskId);
}
