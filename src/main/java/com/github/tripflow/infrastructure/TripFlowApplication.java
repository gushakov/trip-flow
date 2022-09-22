package com.github.tripflow.infrastructure;

import com.github.tripflow.infrastructure.config.TripFlowProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TripFlowProperties.class)
public class TripFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripFlowApplication.class, args);
    }

}
