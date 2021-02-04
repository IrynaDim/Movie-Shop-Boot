package com.dev.cinema.exception;

import lombok.Data;

@Data
public class InternalException {
    private long timestamp = System.currentTimeMillis();
    private int status;
    private String error;
    private String message;
    private String exception;
    private String path;

    public InternalException(String message, Throwable cause, int status, String path) {
        this.message = message;
        this.status = status;
        this.exception = cause.getClass().toString();
        this.error = cause.getMessage();
        this.path = path;
    }
}
