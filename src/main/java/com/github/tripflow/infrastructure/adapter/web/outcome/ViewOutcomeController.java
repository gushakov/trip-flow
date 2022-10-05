package com.github.tripflow.infrastructure.adapter.web.outcome;

import com.github.tripflow.core.usecase.outcome.ViewOutcomeInputPort;
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
public class ViewOutcomeController {
    private final ApplicationContext applicationContext;

    @GetMapping("/viewOutcome")
    @ResponseBody
    public void viewTripBookingOutcome(@RequestParam String taskId) {
        useCase().viewOutcome(Long.valueOf(taskId));
    }

    @PostMapping("/finishProcess")
    @ResponseBody
    public void finishProcess(@RequestParam String taskId) {
        useCase().finishProcess(Long.valueOf(taskId));
    }

    private ViewOutcomeInputPort useCase() {
        return applicationContext.getBean(ViewOutcomeInputPort.class);
    }

}
