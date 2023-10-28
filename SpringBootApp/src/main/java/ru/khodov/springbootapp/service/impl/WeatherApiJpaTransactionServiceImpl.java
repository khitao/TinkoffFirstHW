package ru.khodov.springbootapp.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.khodov.springbootapp.model.City;
import ru.khodov.springbootapp.model.CityWeather;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.service.WeatherApiTransactionService;
import ru.khodov.springbootapp.util.EntityAlreadyExistsException;


@Service
@Qualifier("weatherApiJpaTransactionServiceImpl")
public class WeatherApiJpaTransactionServiceImpl implements WeatherApiTransactionService {

    private final CityServiceImpl cityService;
    private final CityWeatherServiceImpl cityWeatherService;
    private final WeatherTypeServiceImpl weatherTypeService;
    private final Logger logger = LoggerFactory.getLogger(WeatherApiJpaTransactionServiceImpl.class);

    public WeatherApiJpaTransactionServiceImpl(CityServiceImpl cityService, CityWeatherServiceImpl cityWeatherService, WeatherTypeServiceImpl weatherTypeService) {
        this.cityService = cityService;
        this.cityWeatherService = cityWeatherService;
        this.weatherTypeService = weatherTypeService;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void addDataFromWeatherApi(WeatherApi weatherApi) {
        try {
            String name = weatherApi.location().name();

            if (cityService.countByName(name) > 0) {
                throw new EntityAlreadyExistsException("City with name " + name + " already exists.");
            }

            City city = new City();
            city.setName(name);
            City newCity = cityService.createCity(city);

            String type = weatherApi.current().condition().text();

            if (weatherTypeService.countByType(type) > 0) {
                throw new EntityAlreadyExistsException("Weather type " + type + " already exists.") ;
            }

            WeatherType weatherType = new WeatherType();
            weatherType.setType(type);

            WeatherType newWeatherType = weatherTypeService.createWeatherType(weatherType);

            CityWeather cityWeather = new CityWeather();
            cityWeather.setCity(newCity);
            cityWeather.setWeatherType(newWeatherType);
            cityWeather.setTemperature(weatherApi.current().temp_c());
            cityWeather.setDateTime(weatherApi.location().localtime());

            cityWeatherService.createCityWeather(cityWeather);


        } catch (DataAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

