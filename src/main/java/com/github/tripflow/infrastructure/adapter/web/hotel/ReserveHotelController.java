package com.github.tripflow.infrastructure.adapter.web.hotel;

import com.github.tripflow.core.model.hotel.HotelId;
import com.github.tripflow.core.model.trip.TripId;
import com.github.tripflow.core.usecase.hotel.ReserveHotelInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReserveHotelController {

    private final ApplicationContext applicationContext;


    @GetMapping("/reserveHotel")
    @ResponseBody
    public void reserveHotel(@RequestParam String taskId) {
        log.debug("[GET][Reserve hotel] reserving hotel, task ID: {}", taskId);

        useCase().proposeHotelsForSelectionByCustomer(taskId);
    }

    //selectHotelForTrip
    @PostMapping(value = "/selectHotelForTrip")
    @ResponseBody
    public void selectHotelForTrip(@RequestParam String taskId,
                                   @RequestParam Long tripId,
                                   @RequestParam String hotelId){

        useCase().registerSelectedHotelWithTrip(taskId, TripId.of(tripId), HotelId.of(hotelId));

    }

    @PostMapping(value = "/confirmHotelReservation")
    @ResponseBody
    public void confirmHotelReservation(@RequestParam String taskId){
        useCase().confirmHotelReservation(taskId);
    }

    private ReserveHotelInputPort useCase() {
        return applicationContext.getBean(ReserveHotelInputPort.class);
    }

}
