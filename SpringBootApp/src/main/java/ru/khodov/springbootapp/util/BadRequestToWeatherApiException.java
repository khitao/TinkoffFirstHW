package ru.khodov.springbootapp.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BadRequestToWeatherApiException extends RuntimeException {
    private final HttpStatusCode statusCode;

    public BadRequestToWeatherApiException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
