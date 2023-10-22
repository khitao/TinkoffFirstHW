package ru.khodov.springbootapp.service;



import ru.khodov.springbootapp.model.CityWeather;

import java.util.List;

public interface CityWeatherService {
    List<CityWeather> getAllCityWeather();

    CityWeather getCityWeatherById(Long id);

    CityWeather createCityWeather(CityWeather cityWeather);

    CityWeather updateCityWeather(Long id, CityWeather updatedCityWeather);

    void deleteCityWeather(Long id);
}
