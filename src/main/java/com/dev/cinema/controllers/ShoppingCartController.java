package com.dev.cinema.controllers;

import com.dev.cinema.model.*;
import com.dev.cinema.model.dto.ShoppingCartResponseDto;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.mapper.ShoppingCartMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/shopping-carts")
@Api(tags = "/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartMapper mapper;
    private final ShoppingCartService cartService;
    private final MovieSessionService sessionService;

    public ShoppingCartController(ShoppingCartMapper mapper, ShoppingCartService cartService,
                                  MovieSessionService sessionService) {
        this.mapper = mapper;
        this.cartService = cartService;
        this.sessionService = sessionService;
    }

    @GetMapping("/by-user")
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public ShoppingCartResponseDto getUserById(@ApiIgnore Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart cart = cartService.getByUser(user);
        return mapper.convertToResponseDto(cart);
    }

    @PostMapping("/movie-sessions")
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public void addMovieSession(@RequestParam Long sessionId,
                                @ApiIgnore Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MovieSession session = sessionService.getById(sessionId);
        cartService.addSession(session, user);
    }
}
