package com.github.tripflow.infrastructure.adapter.web.flight;

import com.github.tripflow.core.model.flight.FlightNumber;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.usecase.flight.BookFlightInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        useCase.proposeFlightsForSelectionByCustomer(Long.valueOf(taskId));

    }

    @PostMapping(value = "/selectFlightForTrip")
    @ResponseBody
    public void selectFlightForTrip(@RequestParam String taskId,
                                    @RequestParam Long tripId,
                                    @RequestParam String flightNumber) {
        useCase().registerSelectedFlightWithTrip(taskId, TripId.of(tripId), FlightNumber.of(flightNumber));
    }

    @PostMapping(value = "/confirmFlightBooking")
    @ResponseBody
    public void confirmFlightBooking(@RequestParam String taskId) {
        useCase().confirmFlightBooking(Long.valueOf(taskId));
    }


    private BookFlightInputPort useCase() {
        return applicationContext.getBean(BookFlightInputPort.class);
    }

}
