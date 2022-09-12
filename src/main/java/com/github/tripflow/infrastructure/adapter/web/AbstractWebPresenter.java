package com.github.tripflow.infrastructure.adapter.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
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
 * Provides shared mechanism for presenting errors by redirecting to a well-known "/error" path.
 *
 * @see LocalDispatcherServlet
 * @see #redirect(String, Map)
 * @see ErrorController
 */
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractWebPresenter {

    private final LocalDispatcherServlet dispatcher;
    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;

    protected void storeInSession(String attributeName, Object attributeValue) {
        httpRequest.getSession().setAttribute(attributeName, attributeValue);
    }

    public void presentError(Exception e) {

        // if we are here it usually means that we may have an unforeseen
        // error which we may need to log
        log.error(e.getMessage(), e);

        // we need to roll back any active transaction
        try {
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        } catch (NoTransactionException nte) {
            // do nothing if not running in a transactional context
        }

        // redirect to special error handling controller
        redirectError(e.getMessage());
    }

    protected void presentModelAndView(Map<String, Object> responseModel, String viewName) {
        final ModelAndView mav = new ModelAndView(viewName, responseModel);
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
            httpRequest.getSession().setAttribute("lastError", Map.copyOf(params));
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL(uri));
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
