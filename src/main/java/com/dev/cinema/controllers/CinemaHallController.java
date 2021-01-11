package com.dev.cinema.controllers;

import com.dev.cinema.model.dto.CinemaHallRequestDto;
import com.dev.cinema.model.dto.CinemaHallResponseDto;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.mapper.CinemaHallMapper;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cinema-halls")
@Api(tags = "/cinema-halls")
public class CinemaHallController {
    private final CinemaHallService cinemaHallService;
    private final CinemaHallMapper cinemaHallMapper;

    public CinemaHallController(CinemaHallService cinemaHallService,
                                CinemaHallMapper cinemaHallMapper) {
        this.cinemaHallService = cinemaHallService;
        this.cinemaHallMapper = cinemaHallMapper;
    }

    @PostMapping
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public void create(@RequestBody CinemaHallRequestDto cinemaHallDto) {
        cinemaHallService.add(cinemaHallMapper.convertToEntity(cinemaHallDto));
    }

    @GetMapping
    @ApiImplicitParam(name = "token", value = "token", paramType = "query")
    public List<CinemaHallResponseDto> getAll() {
        return cinemaHallService.getAll().stream()
                .map(cinemaHallMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }
}
