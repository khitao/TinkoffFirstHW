package ru.khodov.springbootapp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.khodov.springbootapp.model.Weather;
import ru.khodov.springbootapp.service.WeatherService;


import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @GetMapping("{regionName}")
    public double getTemperatureByRegionNameAndDate(@PathVariable String regionName, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return weatherService.getRegionTemperatureForTheCurrentDate(regionName, dateTime);
    }


    @PostMapping("/{regionName}")
    public void addNewRegion(@PathVariable String regionName, @RequestBody Weather weather) {
        weatherService.addNewRegion(regionName, weather);
    }

    @PutMapping("/{regionName}")
    public void updateRegionTemperature(@PathVariable String regionName, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime, @RequestBody double temperature) {
        weatherService.updateTemperatureInRegion(regionName, dateTime, temperature);
    }


    @DeleteMapping("/{regionName}")
    public void deleteRegion(@PathVariable String regionName) {
        weatherService.deleteRegion(regionName);
    }

}
