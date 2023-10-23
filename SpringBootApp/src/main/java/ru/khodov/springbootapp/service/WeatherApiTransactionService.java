package ru.khodov.springbootapp.service;

import ru.khodov.springbootapp.model.WeatherApi;

public interface WeatherApiTransactionService {
    void addDataFromWeatherApi(WeatherApi weatherApi);
}
