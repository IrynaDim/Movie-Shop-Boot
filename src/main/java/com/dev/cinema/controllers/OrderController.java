package com.dev.cinema.controllers;

import com.dev.cinema.exception.EmptyCartException;
import com.dev.cinema.exception.HttpBadRequestException;
import com.dev.cinema.exception.UserNotFoundException;
import com.dev.cinema.model.*;
import com.dev.cinema.model.dto.OrderResponseDto;
import com.dev.cinema.model.dto.ShoppingCartResponseDto;
import com.dev.cinema.model.dto.UserResponseDto;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import com.dev.cinema.service.mapper.OrderMapper;

import java.util.List;
import java.util.stream.Collectors;

import com.dev.cinema.service.mapper.ShoppingCartMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/orders")
@Api(tags = "/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final ShoppingCartService cartService;
    private final UserService userService;

    public OrderController(OrderService orderService, OrderMapper orderMapper,
                           ShoppingCartService cartService, UserService userService) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/complete")
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public void complete(@ApiIgnore Authentication authentication) throws EmptyCartException {
        User user = (User) authentication.getPrincipal();
        ShoppingCart cart = cartService.getByUser(user);
        List<Ticket> tickets = cart.getTickets();
        if (tickets.size() == 0) {
            throw new EmptyCartException("Your cart is empty. Complete order impossible.");
        }
        orderService.completeOrder(tickets, user);
    }

    @GetMapping
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public List<OrderResponseDto> getOrderHistoryOfUser(@ApiIgnore Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderHistory(user)
                .stream().map(orderMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-email/{email}")
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public List<OrderResponseDto> getUserByEmail(@PathVariable String email,
                                                 @ApiIgnore Authentication authentication)
            throws UserNotFoundException {
        if (authentication.getName().equals(email)) {
            throw new HttpBadRequestException("Admin doesn't have orders.");
        }
        User user = userService.getByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return orderService.getOrderHistory(user)
                .stream().map(orderMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
}
