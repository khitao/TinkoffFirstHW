package ru.khodov.springbootapp.controllers;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.khodov.springbootapp.model.CityWeather;
import ru.khodov.springbootapp.service.CityWeatherService;


import java.util.List;

@RestController
@RequestMapping("/city-weather")
public class CityWeatherController {

    private final CityWeatherService cityWeatherService;

    public CityWeatherController(@Qualifier("jdbcCityWeatherService") CityWeatherService cityWeatherService) {
        this.cityWeatherService = cityWeatherService;
    }

    @GetMapping
    public List<CityWeather> getAllCities() {
        return cityWeatherService.getAllCityWeather();
    }

    @GetMapping("/{id}")
    public CityWeather getCityWeatherById(@PathVariable Long id) {
        return cityWeatherService.getCityWeatherById(id);
    }

    @PostMapping
    public CityWeather createCityWeather(@RequestBody CityWeather cityWeather) {
        return cityWeatherService.createCityWeather(cityWeather);
    }

    @PutMapping("/{id}")
    public CityWeather updateCityWeather(@PathVariable Long id, @RequestBody CityWeather updatedCityWeather) {
        return cityWeatherService.updateCityWeather(id, updatedCityWeather);
    }

    @DeleteMapping("/{id}")
    public void deleteCityWeather(@PathVariable Long id) {
        cityWeatherService.deleteCityWeather(id);
    }
}
