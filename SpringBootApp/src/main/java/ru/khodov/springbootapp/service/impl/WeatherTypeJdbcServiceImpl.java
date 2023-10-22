package ru.khodov.springbootapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.repositories.WeatherTypeJdbcRepository;
import ru.khodov.springbootapp.service.WeatherTypeService;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Qualifier("jdbcWeatherTypeService")
public class WeatherTypeJdbcServiceImpl implements WeatherTypeService {

    private final WeatherTypeJdbcRepository weatherTypeJdbcRepository;

    @Override
    public List<WeatherType> getAllWeatherTypes() {
        return weatherTypeJdbcRepository.getAllWeatherTypes();
    }

    @Override
    public WeatherType getWeatherTypeById(Long id) {
        return weatherTypeJdbcRepository.getWeatherTypeById(id);
    }

    @Override
    public WeatherType createWeatherType(WeatherType weatherType) {
        return weatherTypeJdbcRepository.createWeatherType(weatherType);
    }

    @Override
    public WeatherType updateWeatherType(Long id, WeatherType updatedWeatherType) {
        if (weatherTypeJdbcRepository.getWeatherTypeById(id) == null) {
            throw new NoSuchElementException("No value present");
        }
        return weatherTypeJdbcRepository.updateWeatherType(id, updatedWeatherType);
    }

}
