package com.github.tripflow.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tripflow")
@Getter
@Setter
public class TripFlowProperties {

    private String taskListScheme = "http";
    private String taskListHost = "localhost";
    private int taskListPort = 8081;
    private String taskListClientUserName = "demo";
    private String taskListClientPassword = "demo";

    private int taskListClientMaxRetries = 20;

}
