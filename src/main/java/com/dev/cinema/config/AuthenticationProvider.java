package com.dev.cinema.config;

import com.dev.cinema.model.JwtToken;
import com.dev.cinema.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtToken token = (JwtToken) authentication.getCredentials();

        return authenticationService.findByToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token = " + token));
    }
}
