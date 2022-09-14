package com.github.tripflow.infrastructure.adapter.web.browse;

import com.github.tripflow.core.usecase.browse.BrowseActiveTripsInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BrowseActiveTripsController {

    private final ApplicationContext applicationContext;

    @RequestMapping("/browse-active-trips")
    @ResponseBody
    public void browseActiveTrips(@RequestParam(required = false) String newPik) {

        BrowseActiveTripsInputPort useCase = applicationContext.getBean(BrowseActiveTripsInputPort.class);

        // TODO: pass newPik to the use case and present a message if new trip was just created
        useCase.listTripsByActiveTasksAssignedToUser();
    }

}
