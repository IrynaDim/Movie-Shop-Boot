package com.dev.cinema.service.impl;

import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.Role;
import com.dev.cinema.model.User;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.MovieService;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.RoleService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class InjectData {
    private final UserService userService;
    private final RoleService roleService;
    private final ShoppingCartService shoppingCartService;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;
    private final MovieSessionService movieSessionService;
    private final OrderService orderService;

    public InjectData(UserService userService, RoleService roleService,
                      ShoppingCartService shoppingCartService,
                      MovieService movieService, CinemaHallService cinemaHallService,
                      MovieSessionService movieSessionService, OrderService orderService) {
        this.userService = userService;
        this.roleService = roleService;
        this.shoppingCartService = shoppingCartService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
        this.movieSessionService = movieSessionService;
        this.orderService = orderService;
    }

    @PostConstruct
    public void injectDefaultData() {
        if (userService.getByEmail("admin@gmail.com").isPresent()) {
            return;
        }
        Role adminRole = Role.of("ADMIN");
        Role userRole = Role.of("USER");
        roleService.add(adminRole);
        roleService.add(userRole);

        User user = new User();
        user.setRoles(Set.of(userRole));
        user.setPassword("1111");
        user.setEmail("user@gmail.com");

        User userIra = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);
        userIra.setRoles(roles);
        userIra.setPassword("1111");
        userIra.setEmail("ira@gmail.com");

        User admin = new User();
        admin.setRoles(Set.of(adminRole));
        admin.setPassword("0000");
        admin.setEmail("admin@gmail.com");

        userService.add(user);
        shoppingCartService.registerNewShoppingCart(user);
        userService.add(userIra);
        shoppingCartService.registerNewShoppingCart(userIra);
        userService.add(admin);

        Movie movie = new Movie();
        movie.setTitle("Fast and Furious");
        movie.setDescription("movie about street racing");
        movieService.add(movie);

        Movie movieSecond = new Movie();
        movieSecond.setTitle("Fast and Furious 2");
        movieSecond.setDescription("movie about street racing. second part");
        movieService.add(movieSecond);

        Movie movieThird = new Movie();
        movieThird.setTitle("Fast and Furious 3");
        movieThird.setDescription("movie about street racing. third part");
        movieService.add(movieThird);

        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(100);
        cinemaHall.setDescription("the biggest cinema hall");
        cinemaHallService.add(cinemaHall);
        CinemaHall cinemaHallSecond = new CinemaHall();
        cinemaHallSecond.setCapacity(50);
        cinemaHallSecond.setDescription("the smallest cinema hall");
        cinemaHallService.add(cinemaHallSecond);

        MovieSession movieSession = new MovieSession();
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(20, 00)));
        movieSessionService.add(movieSession);

        MovieSession movieSessionSecond = new MovieSession();
        movieSessionSecond.setCinemaHall(cinemaHallSecond);
        movieSessionSecond.setMovie(movieSecond);
        movieSessionSecond.setShowTime(LocalDateTime.of(LocalDate.of(2020, 10, 07),
                LocalTime.of(20, 00)));
        movieSessionService.add(movieSessionSecond);

        MovieSession movieSessionThird = new MovieSession();
        movieSessionThird.setCinemaHall(cinemaHallSecond);
        movieSessionThird.setMovie(movieThird);
        movieSessionThird.setShowTime(LocalDateTime.of(LocalDate.of(2020, 12, 10),
                LocalTime.of(20, 00)));
        movieSessionService.add(movieSessionThird);

        shoppingCartService.addSession(movieSession, user);
        shoppingCartService.addSession(movieSessionSecond, user);

        shoppingCartService.addSession(movieSessionSecond, userIra);

        orderService.completeOrder(shoppingCartService.getByUser(user).getTickets(), user);
    }
}
