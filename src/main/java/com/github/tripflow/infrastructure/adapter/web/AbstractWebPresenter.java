package com.github.tripflow.infrastructure.adapter.web;

import com.github.tripflow.infrastructure.error.AbstractErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/*
    References:
    ----------

    1.  This class is copied from cargo-clean project: https://github.com/gushakov/cargo-clean.
    2.  Rollback active transaction: https://stackoverflow.com/a/23502214
 */


/**
 * Presents Thymeleaf views by delegating to {@code render()} method of {@link LocalDispatcherServlet}.
 * Concrete Presenters should override this method providing {@code Response Model} and
 * the name of the view to render. Can also be used to "redirect" response to a particular path.
 * <p>
 * Provides shared mechanism for presenting errors by redirecting to a well-known "error" path.
 * <p>
 * Also provides a mechanism for storing "message" into current HTTP session. This can be used
 * from Presenters to store a message to be displayed after the redirects.
 *
 * @see LocalDispatcherServlet
 * @see ErrorController
 * @see #presentModelAndView(Map, String)
 * @see #redirect(String, Map)
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractWebPresenter extends AbstractErrorHandler {

    private final LocalDispatcherServlet dispatcher;
    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;

    protected void message(String text) {
        httpRequest.getSession().setAttribute("message", text);
    }

    private String getMessageFromSession() {
        Object message = httpRequest.getSession().getAttribute("message");
        httpRequest.getSession().removeAttribute("message");
        return (String) message;
    }

    public void presentError(Exception e) {

        // do common error handling
        logErrorAndRollback(e);

        // redirect to special error handling controller
        redirectError(e.getMessage());
    }

    protected void presentModelAndView(Map<String, Object> responseModel, String viewName) {
        final ModelAndView mav = new ModelAndView(viewName, responseModel);
        // check if there is a message in the session and add it to the response model
        String message = getMessageFromSession();
        if (message != null) {
            mav.addObject("message", message);
        }
        try {
            dispatcher.render(mav, httpRequest, httpResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void redirect(String path, Map<String, String> params) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(path);
        params.forEach(uriBuilder::queryParam);
        final String uri = uriBuilder.toUriString();

        try {
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL(uri));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void redirect(String path) {
        try {
            httpResponse.sendRedirect(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void redirectError(String errorMessage) {
        try {
            httpRequest.getSession().setAttribute("errorMessage", errorMessage);
            httpResponse.sendRedirect("error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
