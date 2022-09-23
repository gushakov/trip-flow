package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.GenericTripFlowError;
import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.workflow.TasksOperationsOutputPort;
import com.github.tripflow.core.port.presenter.flight.BookFlightPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class BookFlightUseCase implements BookFlightInputPort {

    private final BookFlightPresenterOutputPort presenter;

    private final SecurityOperationsOutputPort securityOps;
    private final TasksOperationsOutputPort tasksOps;
    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void initializeFlightBookingForCustomer(String taskId) {
        List<Flight> flights;

        try {

            tasksOps.retrieveActiveTaskForAssignee(taskId, securityOps.tripFlowAssigneeRole());

            // load all flights
            flights = dbOps.loadAllFlights();
        } catch (GenericTripFlowError e) {
            presenter.presentError(e);
            return;
        }

        presenter.presentFlightsForSelectionByCustomer(null, flights);


    }
}
