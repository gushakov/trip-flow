package com.github.tripflow.infrastructure.config;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableZeebeClient
public class ZeebeClientConfig {
}
