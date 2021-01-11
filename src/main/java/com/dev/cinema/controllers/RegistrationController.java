package com.dev.cinema.controllers;

import com.dev.cinema.model.Role;
import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.UserRegistrationDto;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import com.dev.cinema.service.mapper.UserMapper;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/registration")
@Api(tags = "/users")
public class RegistrationController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ShoppingCartService shoppingCartService;

    public RegistrationController(UserService userService, UserMapper userMapper,
                                  ShoppingCartService shoppingCartService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping()
    public void addNewUser(@RequestBody @Valid UserRegistrationDto userDto) {
        User user = userMapper.convertToUser(userDto);
        Role userRole = Role.of("USER");
        userRole.setId(2L);
        user.setRoles(Set.of(userRole));
        userService.add(user);
        shoppingCartService.registerNewShoppingCart(user);
    }
}
