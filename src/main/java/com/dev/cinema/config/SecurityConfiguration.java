package com.dev.cinema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/v2/api-docs"),

            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/signin"),
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/greeting"),
            new AntPathRequestMatcher("/registration")
    );

    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    private static final RequestMatcher ALL_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/movies", RequestMethod.GET.name()),
            new AntPathRequestMatcher("/cinema-halls", RequestMethod.GET.name()),
            new AntPathRequestMatcher("/movie-sessions/available", RequestMethod.GET.name())
    );
 
    private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/movies", RequestMethod.POST.name()),
            new AntPathRequestMatcher("/cinema-halls", RequestMethod.POST.name()),
            new AntPathRequestMatcher("/movie-sessions", RequestMethod.POST.name()),
            new AntPathRequestMatcher("/users/by-email/*", RequestMethod.GET.name()),
            new AntPathRequestMatcher("/orders/by-email/*", RequestMethod.GET.name())
    );

    private static final RequestMatcher USER_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/orders/complete", RequestMethod.POST.name()),
            new AntPathRequestMatcher("/orders", RequestMethod.GET.name()),
            new AntPathRequestMatcher("/shopping-carts/by-user", RequestMethod.GET.name()),
            new AntPathRequestMatcher("/shopping-carts/movie-sessions", RequestMethod.POST.name())
    );

    private AuthenticationProvider authenticationProvider;

    @Autowired
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)  // отключается сессия
                .and()
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(ALL_URLS).hasAnyRole("USER", "ADMIN")
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(USER_URLS).hasRole("USER")
                .and()
                .csrf().disable()  // отклбчается защита csrf
                .formLogin().disable() //формы для логина (странички) не будет
                .httpBasic().disable() // авторизация с помощью логин и пароля отключена
                .logout().disable();  // выйти нельзя
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());

        return filter;
    }
//действие в случае успешной авторизации, без переадресации NoRedirectStrategy
    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());

        return successHandler;
    }

    @Bean
    FilterRegistrationBean registration(AuthenticationFilter filter) {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);

        return registration;
    }
}
