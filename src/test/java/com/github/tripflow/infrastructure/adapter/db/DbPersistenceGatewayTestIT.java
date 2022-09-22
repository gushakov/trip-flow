package com.github.tripflow.infrastructure.adapter.db;

import com.github.tripflow.infrastructure.adapter.db.map.MapStructTripDbMapperImpl;
import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import io.camunda.zeebe.spring.client.ZeebeClientSpringConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@JdbcTest
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {ZeebeClientSpringConfiguration.class})})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(JdbcRepositoriesAutoConfiguration.class)
@Import({MapStructTripDbMapperImpl.class, CommonMapStructConverters.class})
public class DbPersistenceGatewayTestIT {

    @Test
    void testSetup() {

    }
}
