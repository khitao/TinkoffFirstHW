package ru.khodov.springbootapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Current(

        String last_updated,
        double temp_c,
        int is_day,
        Condition condition,
        double wind_kph,
        double pressure_mb,
        double precip_mm,
        int humidity,
        int cloud,
        double feelslike_c,
        double vis_km,
        double gust_kph) {
}

