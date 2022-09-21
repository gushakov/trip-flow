package com.github.tripflow.infrastructure.config;

/*
    References:
    ----------

    1. WebSecurityCustomizer, JavaDocs
    2. Spring Security Configuration, Java: https://docs.spring.io/spring-security/reference/servlet/configuration/java.html
    3. Spring Security, override HttpSecurity example: https://github.com/spring-projects/spring-security-samples/blob/main/servlet/java-configuration/max-sessions/src/main/java/example/SecurityConfiguration.java
 */


import com.github.tripflow.core.model.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()

                .and().formLogin(withDefaults())
        ;

        return http.build();
    }

    /*
        Create some users for the application. Use simple in-memory user details
        to be used for demonstration only. Note that the roles need to match the
        values of "Assignee" section in BPMN.
        Note roles are added as granted authorities with "ROLE_" prefix by Spring
        Security by default.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withDefaultPasswordEncoder()
                .username("customer1").password("demo").roles(Constants.ROLE_TRIPFLOW_CUSTOMER.replaceFirst("ROLE_", "")).build());
        return userDetailsManager;
    }

}
