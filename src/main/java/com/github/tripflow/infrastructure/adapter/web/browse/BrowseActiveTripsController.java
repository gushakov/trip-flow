package com.github.tripflow.infrastructure.adapter.web.browse;

import com.github.tripflow.core.usecase.browse.BrowseActiveTripsInputPort;
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
public class BrowseActiveTripsController {

    private final ApplicationContext applicationContext;

    @RequestMapping("/browse-active-trips")
    @ResponseBody
    public void browseActiveTrips(@RequestParam(required = false) String newPik) {

        BrowseActiveTripsInputPort useCase = applicationContext.getBean(BrowseActiveTripsInputPort.class);

        useCase.listTripsByActiveTasksAssignedToUser(newPik);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/continueTripTask",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public void continueTripTask(){
        log.debug("[POST] continueTripTask");
    }

}
