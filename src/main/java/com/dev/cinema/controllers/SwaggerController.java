package com.dev.cinema.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SwaggerController {
    @GetMapping("/")
    RedirectView getUI() {
        return new RedirectView("/swagger-ui.html#/");
    }
}
