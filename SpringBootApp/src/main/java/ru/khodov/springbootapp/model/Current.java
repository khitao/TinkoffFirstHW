package ru.khodov.springbootapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {
    private String last_updated;
    private double temp_c;
    private int is_day;
    private Condition condition;
    private double wind_kph;
    private double pressure_mb;
    private double precip_mm;
    private int humidity;
    private int cloud;
    private double feelslike_c;
    private double vis_km;
    private double gust_kph;
}
