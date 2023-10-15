package ru.khodov.springbootapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.CityWeather;

import ru.khodov.springbootapp.repositories.CityWeatherJdbcRepository;
import ru.khodov.springbootapp.service.CityWeatherService;

import java.util.List;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Service
@Qualifier("jdbcCityWeatherService")
public class CityWeatherJdbcServiceImpl implements CityWeatherService {

    private final CityWeatherJdbcRepository cityWeatherJdbcRepository;

    @Override
    public List<CityWeather> getAllCityWeather() {
        return cityWeatherJdbcRepository.getAllCityWeather();
    }

    @Override
    public CityWeather getCityWeatherById(Long id) {
        return cityWeatherJdbcRepository.getCityWeatherById(id);
    }

    @Override
    public CityWeather createCityWeather(CityWeather cityWeather) {
        return cityWeatherJdbcRepository.createCityWeather(cityWeather);
    }

    @Override
    public CityWeather updateCityWeather(Long id, CityWeather updatedCityWeather) {
        if (cityWeatherJdbcRepository.getCityWeatherById(id) == null) {
            throw new NoSuchElementException("No value present");
        }
        return cityWeatherJdbcRepository.updateCityWeather(id, updatedCityWeather);
    }

    @Override
    public void deleteCityWeather(Long id) {
        cityWeatherJdbcRepository.deleteCityWeather(id);
    }
}
