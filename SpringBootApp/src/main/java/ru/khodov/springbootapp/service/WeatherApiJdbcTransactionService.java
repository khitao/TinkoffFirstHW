package ru.khodov.springbootapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.khodov.springbootapp.model.City;
import ru.khodov.springbootapp.model.CityWeather;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.service.impl.CityJdbcServiceImpl;
import ru.khodov.springbootapp.service.impl.CityWeatherJdbcServiceImpl;
import ru.khodov.springbootapp.service.impl.WeatherTypeJdbcServiceImpl;
import ru.khodov.springbootapp.util.EntityAlreadyExistsException;

@Service
public class WeatherApiJdbcTransactionService {
    private final PlatformTransactionManager transactionManager;
    private final CityJdbcServiceImpl cityJdbcService;

    private final CityWeatherJdbcServiceImpl cityWeatherJdbcService;

    private final WeatherTypeJdbcServiceImpl weatherTypeJdbcService;

    private final Logger logger = LoggerFactory.getLogger(WeatherApiJdbcTransactionService.class);


    public WeatherApiJdbcTransactionService(PlatformTransactionManager transactionManager, CityJdbcServiceImpl cityJdbcService, CityWeatherJdbcServiceImpl cityWeatherJdbcService,
                                            WeatherTypeJdbcServiceImpl weatherTypeJdbcService) {
        this.transactionManager = transactionManager;
        this.cityJdbcService = cityJdbcService;
        this.cityWeatherJdbcService = cityWeatherJdbcService;
        this.weatherTypeJdbcService = weatherTypeJdbcService;
    }

    public void addDataFromWeatherApi(WeatherApi weatherApi) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        TransactionStatus transaction = transactionManager.getTransaction(def);

        try {

            String name = weatherApi.location().name();

            if (cityJdbcService.countByName(name) > 0) {
                throw new EntityAlreadyExistsException("City with name " + name + " already exists.") ;
            }

            City city = new City();
            city.setName(name);
            City newCity = cityJdbcService.createCity(city);

            String type = weatherApi.current().condition().text();

            if (weatherTypeJdbcService.countByType(type) > 0) {
                throw new EntityAlreadyExistsException("Weather type " + type + " already exists.");
            }

            WeatherType weatherType = new WeatherType();
            weatherType.setType(type);

            WeatherType newWeatherType = weatherTypeJdbcService.createWeatherType(weatherType);

            CityWeather cityWeather = new CityWeather();
            cityWeather.setCity(newCity);
            cityWeather.setWeatherType(newWeatherType);
            cityWeather.setTemperature(weatherApi.current().temp_c());
            cityWeather.setDateTime(weatherApi.location().localtime());

            cityWeatherJdbcService.createCityWeather(cityWeather);

            transactionManager.commit(transaction);


        } catch (DataAccessException e) {
            transactionManager.rollback(transaction);
            logger.error(e.getMessage(), e);
        }

    }


}
