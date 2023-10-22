package ru.khodov.springbootapp.controllers;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.service.WeatherTypeService;


import java.util.List;


@RequestMapping("/weather-types")
@RestController
public class WeatherTypeController {

    private final WeatherTypeService weatherTypeService;

    public WeatherTypeController(@Qualifier("jdbcWeatherTypeService") WeatherTypeService weatherTypeService) {
        this.weatherTypeService = weatherTypeService;
    }

    @GetMapping
    public List<WeatherType> getAllWeatherTypes() {
        return weatherTypeService.getAllWeatherTypes();
    }

    @GetMapping("/{id}")
    public WeatherType getWeatherTypeById(@PathVariable Long id) {
        return weatherTypeService.getWeatherTypeById(id);
    }

    @PostMapping
    public WeatherType createWeatherType(@RequestBody WeatherType weatherType) {
        return weatherTypeService.createWeatherType(weatherType);
    }

    @PutMapping("/{id}")
    public WeatherType updateWeatherType(@PathVariable Long id, @RequestBody WeatherType updatedWeatherType) {
        return weatherTypeService.updateWeatherType(id, updatedWeatherType);
    }

}
