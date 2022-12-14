package com.github.tripflow.infrastructure.adapter.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/*
    References:
    ----------

    1. This class is copied from cargo-clean project: https://github.com/gushakov/cargo-clean
 */

@Controller
@Slf4j
public class ErrorController {

    /*
        When handling errors, we are using standard way to resolve views in Spring MVC.
     */

    @GetMapping("/error")
    public String onError(@SessionAttribute(required = false) String errorMessage, Model model,
                          HttpServletRequest request) {

        log.error("Error occurred: %s".formatted(errorMessage));

        model.addAttribute("errorMessage",
                Objects.requireNonNullElse(errorMessage, "Unknown error"));

        request.getSession().removeAttribute("errorMessage");

        return "error";
    }

}
