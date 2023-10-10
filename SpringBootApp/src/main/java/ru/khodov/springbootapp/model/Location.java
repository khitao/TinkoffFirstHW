package ru.khodov.springbootapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Location(
        String name,
        String region,
        String country,
        String tz_id,
        @JsonFormat(pattern = "yyyy-MM-dd H:mm")
        LocalDateTime localtime
) {}
