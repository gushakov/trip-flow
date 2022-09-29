package com.github.tripflow.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = {"com.github.tripflow.infrastructure.adapter.db"})
public class JdbcConfig {
}
