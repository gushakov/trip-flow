package com.github.tripflow.core.usecase.flight;

import com.github.tripflow.core.model.trip.Trip;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.core.port.presenter.flight.BookFlightPresenterOutputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class BookFlightUseCase implements BookFlightInputPort {

    private final BookFlightPresenterOutputPort presenter;
    private final DbPersistenceOperationsOutputPort dbOps;

    @Override
    public void initializeFlightBookingForCustomer(TripId tripId) {

        Trip trip = dbOps.loadTrip(tripId);

        log.debug("[BookFlightUseCase] Loaded trip: {}", trip);



    }
}
