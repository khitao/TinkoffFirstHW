package ru.khodov.springbootapp.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WeatherDto {


    private String regionName;
    private double temperature;


    public WeatherDto(String regionName, double temperature) {
        this.regionName = regionName;
        this.temperature = temperature;
    }

}
