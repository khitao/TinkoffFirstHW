package ru.khodov.springbootapp.util;

public class ManyRequestsException extends RuntimeException {
    public ManyRequestsException(String message) {
        super(message);
    }
}
