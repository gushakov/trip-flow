package com.github.tripflow.infrastructure.adapter.web.browse;

import com.github.tripflow.core.usecase.browse.BrowseActiveTripsInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BrowseActiveTripsController {

    private final ApplicationContext applicationContext;

    @GetMapping("/browseActiveTrips")
    @ResponseBody
    public void browseActiveTrips() {

        BrowseActiveTripsInputPort useCase = applicationContext.getBean(BrowseActiveTripsInputPort.class);

        useCase.listEntriesForActiveTripsStartedByUser();
    }

}
