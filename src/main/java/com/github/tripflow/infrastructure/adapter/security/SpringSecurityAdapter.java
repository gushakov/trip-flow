package com.github.tripflow.infrastructure.adapter.security;

import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.TripFlowSecurityError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityAdapter implements SecurityOperationsOutputPort {
    @Override
    public String loggedInUserName() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()){
            throw new TripFlowSecurityError("User is not authenticated");
        }

        return ((User) authentication.getPrincipal()).getUsername();
    }
}
