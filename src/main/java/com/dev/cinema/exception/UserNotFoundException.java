package com.dev.cinema.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String massage) {
        super(massage);
    }
}