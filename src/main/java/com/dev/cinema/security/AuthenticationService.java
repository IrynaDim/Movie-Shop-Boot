package com.dev.cinema.security;

import com.dev.cinema.model.JwtToken;
import com.dev.cinema.model.User;
import com.dev.cinema.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JWTTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserService userService, JWTTokenService jwtTokenService,
                                 PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByToken(JwtToken token) {
        return Optional.ofNullable(jwtTokenService.verify(token).get("id"))
                .map(Long::valueOf)
                .flatMap(userService::getById);
    }

    public JwtToken signIn(String email, String password) {
        User user = userService.getByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User with email "
                        + email + " not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            Map<String, String> userData = new HashMap<>();
            userData.put("id", user.getId().toString());
            return jwtTokenService.expiring(userData);
        }
        return null;
    }
}