package com.dev.cinema.controllers;

import com.dev.cinema.exception.UserNotFoundException;
import com.dev.cinema.model.User;
import com.dev.cinema.model.dto.UserResponseDto;
import com.dev.cinema.service.UserService;
import com.dev.cinema.service.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Api(tags = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/by-email/{email}")
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public UserResponseDto getUserByEmail(@PathVariable String email) throws UserNotFoundException {
        User userToFind = userService.getByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return userMapper.convertToResponseDto(userToFind);
    }
}
