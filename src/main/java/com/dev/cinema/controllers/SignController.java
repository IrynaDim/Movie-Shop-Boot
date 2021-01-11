package com.dev.cinema.controllers;

import com.dev.cinema.exception.HttpBadRequestException;
import com.dev.cinema.model.JwtToken;
import com.dev.cinema.security.AuthenticationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
@Api(tags = "/sign-in")
public class SignController {
    private final AuthenticationService authenticationService;

    @Autowired
    public SignController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping()
    public JwtToken in(@RequestParam("email") String email,
                       @RequestParam("password") String password) {
        JwtToken jwtToken = authenticationService.signIn(email, password);
        if (jwtToken == null) {
            throw new HttpBadRequestException("The email/password is invalid");
        }
        return jwtToken;
    }
}
