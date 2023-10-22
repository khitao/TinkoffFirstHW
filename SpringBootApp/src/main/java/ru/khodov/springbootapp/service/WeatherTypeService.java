package ru.khodov.springbootapp.service;


import ru.khodov.springbootapp.model.WeatherType;

import java.util.List;

public interface WeatherTypeService {
    List<WeatherType> getAllWeatherTypes();

    WeatherType updateWeatherType(Long id, WeatherType updatedWeatherType);

    WeatherType getWeatherTypeById(Long id);

    WeatherType createWeatherType(WeatherType weatherType);
}
