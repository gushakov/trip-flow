package com.github.tripflow.infrastructure.adapter.web.home;

import com.github.tripflow.core.usecase.home.WelcomeInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/*
    References:
    ----------

    1.  Spring MVC, redirect on empty path: https://stackoverflow.com/questions/16175035/spring-requestmapping-for-empty-servlet-path-is-not-working
 */

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ApplicationContext applicationContext;

    @RequestMapping(method = RequestMethod.GET)
    public String emptyPathRedirect() {
        return "redirect:/";
    }

    /**
     * Entry point into the application (after the login).
     */
    @RequestMapping("/")
    @ResponseBody
    public void welcome() {
        useCase().welcomeUser();
    }

    @RequestMapping(value = "/createNewTripBooking", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public void createNewTripBooking() {
        useCase().startNewTripBooking();
    }

    private WelcomeInputPort useCase() {
        return applicationContext.getBean(WelcomeInputPort.class);
    }
}
