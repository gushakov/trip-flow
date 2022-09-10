package com.github.tripflow.infrastructure.adapter.web.controller.home;

import com.github.tripflow.core.usecase.home.WelcomeInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ApplicationContext applicationContext;

    /**
     * Entry point into the application (after the login).
     */
    @RequestMapping("/")
    @ResponseBody
    public void welcome() {

        WelcomeInputPort useCase = applicationContext.getBean(WelcomeInputPort.class);
        useCase.welcomeUser();
    }
}
