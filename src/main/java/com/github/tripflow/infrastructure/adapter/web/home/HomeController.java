package com.github.tripflow.infrastructure.adapter.web.home;

import com.github.tripflow.core.usecase.home.WelcomeInputPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    References:
    ----------

    1.  Spring MVC, redirect on empty path: https://stackoverflow.com/questions/16175035/spring-requestmapping-for-empty-servlet-path-is-not-working
    2.  Spring MVC, redirect to external URL: https://stackoverflow.com/questions/17955777/redirect-to-an-external-url-from-controller-action-in-spring-mvc
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

    /**
     * Will forward POST request to provided {@code action} parameter.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/forwardActionRequest",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public void forwardActionRequest(@RequestParam String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("[Forward request] action: {}", action);
        request.getRequestDispatcher(action).forward(request, response);
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
