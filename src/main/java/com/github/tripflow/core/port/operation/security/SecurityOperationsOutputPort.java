package com.github.tripflow.core.port.operation.security;

import com.github.tripflow.core.model.Constants;

public interface SecurityOperationsOutputPort {

    String loggedInUserName();

    String tripFlowAssigneeRole();

    void assertCustomerPermission(String username);

    default boolean isLoggedInUserCustomer() {
        return tripFlowAssigneeRole().equals(Constants.ROLE_TRIPFLOW_CUSTOMER);
    }

}
