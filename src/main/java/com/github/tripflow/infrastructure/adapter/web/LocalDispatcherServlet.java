package com.github.tripflow.infrastructure.adapter.web;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    Notes:
    -----

    1.  We are overriding the default DispatcherServlet so that we can have access to "render" (protected) method.


    References:
    ----------

    1.  This class is copied from cargo-clean project: https://github.com/gushakov/cargo-clean
    2.  How to access Spring's DispatcherServlet: https://stackoverflow.com/questions/68376668/how-to-get-the-dispatcherservlet-from-spring-webapplicationcontext
 */

public class LocalDispatcherServlet extends DispatcherServlet {

    /*
        We will be calling this method from Presenters.
     */
    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.render(mv, request, response);
    }

}
