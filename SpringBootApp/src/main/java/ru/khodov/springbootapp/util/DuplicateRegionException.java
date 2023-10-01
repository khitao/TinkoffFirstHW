package ru.khodov.springbootapp.util;

public class DuplicateRegionException extends RuntimeException {
    public DuplicateRegionException(String message) {
        super(message);
    }
}
