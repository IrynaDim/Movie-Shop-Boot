package com.dev.cinema.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(new Tag("name", "description"), getTags())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .build()
                .apiInfo(apiInfo());
    }

    private Tag[] getTags() {
        return new Tag[]{
                new Tag("/users", "Work with users"),
                new Tag("/cinema-halls", "Work with cinema-halls"),
                new Tag("/movies", "Work with movies"),
                new Tag("/movie-sessions", "Work with movie-sessions"),
                new Tag("/orders", "Work with user order"),
                new Tag("/shopping-carts", "Work with user shopping-cart"),
                new Tag("/greeting", "Greeting for guests"),
                new Tag("/sign-in", "Sign in to get token"),
        };
    }

    private springfox.documentation.service.ApiInfo apiInfo() {
        return new springfox.documentation.builders.ApiInfoBuilder()
                .title(getTitle())
                .description("API for movie shop")
                .build();
    }

    public String getTitle() {
        return "Movie Shop API";
    }
}
