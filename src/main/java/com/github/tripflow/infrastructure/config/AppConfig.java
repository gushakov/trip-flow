package com.github.tripflow.infrastructure.config;

import com.github.tripflow.core.port.operation.workflow.TaskNotFoundError;
import com.github.tripflow.infrastructure.adapter.web.LocalDispatcherServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;
import org.springframework.web.servlet.DispatcherServlet;

/*
    References:

    1. Some configuration is copied from cargo-clean project: https://github.com/gushakov/cargo-clean
    2. How to access Spring's DispatcherServlet (answer): https://stackoverflow.com/a/68536242
 */

@Configuration
public class AppConfig {

    /*
        We need to register our custom override of DispatcherServlet
        instead of original dispatcher.
    */

    @Bean
    public ServletRegistrationBean localDispatcherRegistration() {
        return new ServletRegistrationBean<>(dispatcherServlet());
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    @Qualifier("local")
    public DispatcherServlet dispatcherServlet() {
        return new LocalDispatcherServlet();
    }

    // Spring RetryTemplate for TaskList client

    @Bean
    @Qualifier("taskListClient")
    public RetryTemplate taskListClientRetryTemplate(TripFlowProperties tripFlowProps) {
        return new RetryTemplateBuilder()
                .fixedBackoff(500L)
                .retryOn(TaskNotFoundError.class)
                .maxAttempts(tripFlowProps.getTaskListClientMaxRetries())
                .build();
    }

}
