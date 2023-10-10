package ru.khodov.springbootapp.util;

import lombok.Data;

@Data
public class WeatherApiError {
    private ErrorDetail error;
}
