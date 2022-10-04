package com.github.tripflow.core.port.operation.security;

public interface SecurityOperationsOutputPort {

    String loggedInUserName();

    String tripFlowAssigneeRole();

    void assertCustomerPermission(String username);

}
