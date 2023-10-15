package ru.khodov.springbootapp.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.CityWeather;
import ru.khodov.springbootapp.repositories.CityWeatherRepository;
import ru.khodov.springbootapp.service.CityWeatherService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Qualifier("jpaCityWeatherService")
public class CityWeatherServiceImpl implements CityWeatherService {


    private final CityWeatherRepository cityWeatherRepository;


    public List<CityWeather> getAllCityWeather() {
        return cityWeatherRepository.findAll();
    }

    public CityWeather getCityWeatherById(Long id) {
        return cityWeatherRepository.findById(id).orElseThrow();
    }

    public CityWeather createCityWeather(CityWeather cityWeather) {
        return cityWeatherRepository.save(cityWeather);
    }

    public CityWeather updateCityWeather(Long id, CityWeather updatedCityWeather) {
        CityWeather existingCityWeather = cityWeatherRepository.findById(id).orElseThrow();

        existingCityWeather.setDateTime(updatedCityWeather.getDateTime());
        existingCityWeather.setTemperature(updatedCityWeather.getTemperature());
        existingCityWeather.setCity(updatedCityWeather.getCity());
        existingCityWeather.setWeatherType(updatedCityWeather.getWeatherType());

        return cityWeatherRepository.save(existingCityWeather);
    }

    public void deleteCityWeather(Long id) {
        cityWeatherRepository.deleteById(id);
    }
}
