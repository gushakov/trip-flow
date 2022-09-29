package com.github.tripflow.infrastructure.adapter.db;

import com.github.tripflow.core.model.flight.Flight;
import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.port.operation.db.DbPersistenceOperationsOutputPort;
import com.github.tripflow.infrastructure.adapter.db.map.MapStructTripFlowDbMapperImpl;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(JdbcRepositoriesAutoConfiguration.class)
@Import({MapStructTripFlowDbMapperImpl.class,
        CommonMapStructConverters.class,
        DbPersistenceGateway.class})
public class DbPersistenceGatewayTestIT {

    @Autowired
    private DbPersistenceOperationsOutputPort dbOps;

    @Test
    void load_all_flights_must_return_some_flights() {
        List<Flight> flights = dbOps.loadAllFlights();
        Assertions.assertThat(flights).isNotEmpty();
        Assertions.assertThat(getOneFlight(flights, "LX1712"))
                .extracting(Flight::getFlightNumber, Flight::getAirline, Flight::getOriginCity,
                        Flight::getOriginIataCode, Flight::getDestinationCity, Flight::getDestinationIataCode,
                        Flight::getPrice)
                .containsExactly(FlightNumber.of("LX1712"), "SWISS", "Zurich", "ZRH", "Naples", "NAP", 301);
    }

    @NotNull
    private static Flight getOneFlight(List<Flight> flights, String flightNumber) {
        return flights.stream()
                .filter(flight -> flight.getFlightNumber()
                        .getNumber().equals(flightNumber))
                .findAny()
                .orElseThrow(AssertionError::new);
    }
}
