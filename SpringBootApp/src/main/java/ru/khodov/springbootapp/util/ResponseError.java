package ru.khodov.springbootapp.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;


import lombok.Getter;

@Getter
public class ResponseError {
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime time = LocalDateTime.now();

    public ResponseError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
