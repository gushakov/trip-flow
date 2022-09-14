package com.github.tripflow.infrastructure.adapter.security;

import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.TripFlowSecurityError;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityAdapter implements SecurityOperationsOutputPort {
    @Override
    public String loggedInUserName() {

        Authentication authentication = authentication();

        return ((User) authentication.getPrincipal()).getUsername();
    }

    @Override
    public String tripFlowAssigneeRole() {

        Authentication authentication = authentication();

        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_TRIPFLOW_"))
                .findAny().orElseThrow(() -> new TripFlowSecurityError("User does not have any roles associated with TripFlow"));
    }

    @NotNull
    private Authentication authentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new TripFlowSecurityError("User is not authenticated");
        }
        return authentication;
    }
}
