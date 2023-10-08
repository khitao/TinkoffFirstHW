package ru.khodov.springbootapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    private String name;
    private String region;
    private String country;
    private String tz_id;
    private String localtime;
}