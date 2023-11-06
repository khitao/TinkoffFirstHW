package ru.khodov.springbootapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record Condition(String text) {}