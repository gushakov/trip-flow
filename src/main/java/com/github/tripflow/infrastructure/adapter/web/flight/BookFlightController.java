package com.github.tripflow.infrastructure.adapter.web.flight;

import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.usecase.flight.BookFlightInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookFlightController {

    private final ApplicationContext applicationContext;

    @GetMapping("/bookFlight")
    @ResponseBody
    public void bookFlight(@RequestParam String taskId) {

        log.debug("[GET][Book flight] booking flight, task ID: {}", taskId);

        BookFlightInputPort useCase = useCase();
        useCase.proposeFlightsForSelectionByCustomer(taskId);

    }

    @PostMapping(value = "/selectFlightForTrip", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public void selectFlightForTrip(@RequestParam String taskId,
                                    @RequestParam Long tripId,
                                    @RequestParam String flightNumber) {
        useCase().registerSelectedFlightWithTrip(taskId, TripId.of(tripId), FlightNumber.of(flightNumber));
    }

    @PostMapping(value = "/confirmFlightBooking", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public void confirmFlightBooking(@RequestParam String taskId) {
        useCase().confirmFlightBooking(taskId);
    }


    @NotNull
    private BookFlightInputPort useCase() {
        return applicationContext.getBean(BookFlightInputPort.class);
    }

}
