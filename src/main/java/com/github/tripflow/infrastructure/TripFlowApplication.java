package com.github.tripflow.infrastructure;

import com.github.tripflow.infrastructure.config.TripFlowProperties;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableZeebeClient
@EnableJdbcRepositories
@EnableConfigurationProperties(TripFlowProperties.class)
public class TripFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripFlowApplication.class, args);
    }

}
