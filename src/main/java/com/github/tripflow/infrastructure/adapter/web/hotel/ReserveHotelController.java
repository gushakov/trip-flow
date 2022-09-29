package com.github.tripflow.infrastructure.adapter.web.hotel;

import com.github.tripflow.core.usecase.hotel.ReserveHotelInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReserveHotelController {

    private final ApplicationContext applicationContext;


    @GetMapping("/bookHotel")
    @ResponseBody
    public void bookHotel(@RequestParam String taskId){
        log.debug("[GET][Book hotel] booking hotel, task ID: {}", taskId);

        useCase().proposeHotelsForSelectionByCustomer(taskId);
    }

    private ReserveHotelInputPort useCase(){
        return applicationContext.getBean(ReserveHotelInputPort.class);
    }

}
