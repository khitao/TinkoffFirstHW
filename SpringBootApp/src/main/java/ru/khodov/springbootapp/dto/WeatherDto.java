package ru.khodov.springbootapp.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WeatherDto {

    private UUID regionId;
    private String regionName;
    private double temperature;
    private LocalDateTime dateTime;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;


    public WeatherDto(UUID regionId, String regionName, double temperature, LocalDateTime dateTime) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.temperature = temperature;
        this.dateTime = dateTime;
    }

}
