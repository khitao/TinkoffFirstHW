package ru.khodov.springbootapp.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.service.WeatherApiTransactionService;
import ru.khodov.springbootapp.service.WeatherApiService;

@RestController
@RequestMapping("/weather_api/current")
public class WeatherApiController {

    private final WeatherApiService weatherApiClient;
    private final WeatherApiTransactionService weatherApiTransactionService;

    public WeatherApiController(WeatherApiService weatherApiClient,
                                @Qualifier("weatherApiJdbcTransactionServiceImpl") WeatherApiTransactionService weatherApiTransactionService) {
        this.weatherApiClient = weatherApiClient;
        this.weatherApiTransactionService = weatherApiTransactionService;
    }

    @GetMapping("/{location}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public WeatherApi getCurrentWeather(@PathVariable String location) {
        WeatherApi weatherApi = weatherApiClient.getCurrentWeather(location);
        weatherApiTransactionService.addDataFromWeatherApi(weatherApi);
        return weatherApi;
    }

}
