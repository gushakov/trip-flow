package com.github.tripflow.infrastructure.adapter.security;

import com.github.tripflow.core.model.Constants;
import com.github.tripflow.core.port.operation.security.SecurityOperationsOutputPort;
import com.github.tripflow.core.port.operation.security.TripFlowSecurityError;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringSecurityAdapter implements SecurityOperationsOutputPort {

    private final UserDetailsService userDetailsService;

    @Override
    public String loggedInUserName() {

        try {
            Authentication authentication = authentication();

            return ((User) authentication.getPrincipal()).getUsername();
        } catch (Exception e) {
            throw new TripFlowSecurityError(e);
        }
    }

    @Override
    public String tripFlowAssigneeRole() {

        Authentication authentication = authentication();

        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_TRIPFLOW_"))
                .findAny().orElseThrow(() -> new TripFlowSecurityError("User does not have any roles associated with TripFlow"));
    }

    @Override
    public void assertCustomerPermission(String username) {
        if (userDetailsService.loadUserByUsername(username).getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(role -> role.equals(Constants.ROLE_TRIPFLOW_CUSTOMER))) {
            throw new TripFlowSecurityError("User %s does not have a \"customer\" role"
                    .formatted(username));
        }
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
