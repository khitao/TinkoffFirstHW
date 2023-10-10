package ru.khodov.springbootapp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.service.WeatherApiService;

@RestController
@RequestMapping("/weather_api/current")
@RequiredArgsConstructor
public class WeatherApiController {

    private final WeatherApiService weatherApiClient;

    @GetMapping("{location}")
    public Mono<ResponseEntity<WeatherApi>> getCurrentWeather(@PathVariable String location) {
        return weatherApiClient.getCurrentWeather(location);
    }

}
