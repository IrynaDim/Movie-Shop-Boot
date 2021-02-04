package com.dev.cinema.controllers;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
@Api(tags = "/greeting")
public class HelloController {

    @GetMapping
    public String sayHello() {
        return "Hello!";
    }
}
