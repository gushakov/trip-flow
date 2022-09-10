package com.github.tripflow.infrastructure.adapter.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/*
    References:
    ----------

    1. This class is copied from cargo-clean project. See https://github.com/gushakov/cargo-clean.
 */

@Controller
public class ErrorController {

    /*
        When handling errors, we are using standard way to resolve views in Spring MVC.
     */

    @RequestMapping("/error")
    public String onError(@SessionAttribute(required = false) String errorMessage, Model model,
                          HttpServletRequest request) {

        model.addAttribute("errorMessage",
                Objects.requireNonNullElse(errorMessage, "Unknown error"));

        request.getSession().removeAttribute("errorMessage");

        return "error";
    }

}
