package com.github.tripflow.infrastructure.adapter.web.summary;

import com.github.tripflow.core.usecase.summary.ViewSummaryInputPort;
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
public class ViewSummaryController {

    private final ApplicationContext applicationContext;

    @GetMapping("/viewSummary")
    @ResponseBody
    public void viewSummary(@RequestParam String taskId) {
        useCase().viewTripSummary(taskId);
    }

    @PostMapping(value = "/proceedWithPayment")
    @ResponseBody
    public void proceedWithPayment(@RequestParam String taskId){
        useCase().proceedWithPayment(taskId);
    }

    private ViewSummaryInputPort useCase() {
        return applicationContext.getBean(ViewSummaryInputPort.class);
    }

}
