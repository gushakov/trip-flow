package com.github.tripflow.infrastructure;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
public class TripFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripFlowApplication.class, args);
    }

}
