package com.github.tripflow.infrastructure.adapter.web.browse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BrowseActiveTripsController {

    @RequestMapping("/browse-trip-bookings")
    public String browseTripBookings(@RequestParam(required = false) String newPik){
        return "browse-trip-bookings";
    }

}
