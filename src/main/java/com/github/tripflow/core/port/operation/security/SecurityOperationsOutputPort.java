package com.github.tripflow.core.port.operation.security;

import com.github.tripflow.core.model.Constants;

public interface SecurityOperationsOutputPort {

    String loggedInUserName();

    String tripFlowAssigneeRole();

    default boolean isCustomer() {
        return tripFlowAssigneeRole().equals(Constants.ROLE_TRIPFLOW_CUSTOMER);
    }

}
