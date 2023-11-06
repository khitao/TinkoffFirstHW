package ru.khodov.springbootapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;


import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record Location(
        String name,
        String region,
        String country,
        @JsonFormat(pattern = "yyyy-MM-dd H:mm")
        LocalDateTime localtime
) {}
