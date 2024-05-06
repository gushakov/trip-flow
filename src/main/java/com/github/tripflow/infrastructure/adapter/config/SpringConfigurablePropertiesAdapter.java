package com.github.tripflow.infrastructure.adapter.config;

import com.github.tripflow.core.port.config.ConfigurationOperationsOutputPort;
import com.github.tripflow.infrastructure.config.TripFlowProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringConfigurablePropertiesAdapter implements ConfigurationOperationsOutputPort {

    private final TripFlowProperties tripFlowProps;

    @Override
    public int obtainCreditLimitForCustomer(String customer) {
        return tripFlowProps.getCreditLimit().getOrDefault(customer, 400);
    }
}
