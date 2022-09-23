package com.github.tripflow.infrastructure.adapter.web.flight;

import com.github.tripflow.core.usecase.flight.BookFlightInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookFlightController {

    private final ApplicationContext applicationContext;

    @RequestMapping("/bookFlight")
    @ResponseBody
    public void bookFlight(@RequestParam String taskId) {

        log.debug("[GET][Book flight] booking flight, task ID: {}", taskId);

        BookFlightInputPort useCase = applicationContext.getBean(BookFlightInputPort.class);

        useCase.initializeFlightBookingForCustomer(taskId);

    }

    @RequestMapping(value = "/selectFlightForTrip", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public void selectFlightForTrip(@RequestParam Long tripId, @RequestParam String flightNumber) {
        System.out.println("----->"+flightNumber);
    }

}
