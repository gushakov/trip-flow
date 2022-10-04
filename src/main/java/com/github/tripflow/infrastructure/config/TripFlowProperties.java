package com.github.tripflow.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "tripflow")
@Getter
@Setter
public class TripFlowProperties {

    private int taskLookUpMaxRetries = 5;

    private Map<String, Integer> creditLimit = Map.of("customer1", 500, "customer2", 400);

}
