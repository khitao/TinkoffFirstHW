package ru.khodov.springbootapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeatherDto {

    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

}
