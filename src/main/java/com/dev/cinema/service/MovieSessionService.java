package com.dev.cinema.service;

import com.dev.cinema.model.MovieSession;
import java.time.LocalDate;
import java.util.List;

public interface MovieSessionService {
    List<MovieSession> getAvailableSessions(LocalDate date);

    MovieSession add(MovieSession movieSession);

    MovieSession getById(Long id);
}
