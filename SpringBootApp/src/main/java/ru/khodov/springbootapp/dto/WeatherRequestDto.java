package ru.khodov.springbootapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherRequestDto {
    private double temperature;
    private LocalDateTime dateTime;


}
