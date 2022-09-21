package com.github.tripflow.infrastructure.adapter.web.flight;

import com.github.tripflow.core.model.trip.TripId;
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

    @RequestMapping(value = "/book-flight", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public void bookFlight(@RequestParam Long tripId) {

        log.debug("[POST][Book flight] booking flight, trip ID: {}", tripId);

        BookFlightInputPort useCase = applicationContext.getBean(BookFlightInputPort.class);

        useCase.initializeFlightBookingForCustomer(TripId.of(tripId));

    }

}
