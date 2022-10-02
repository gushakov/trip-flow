package com.github.tripflow.core.usecase.workflow;

import com.github.tripflow.core.model.trip.TripId;

public interface ContinueWorkflowInputPort {

        void continueToNextActiveTaskForUser(TripId tripId);

}
