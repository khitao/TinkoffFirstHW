package ru.khodov.springbootapp.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.City;
import ru.khodov.springbootapp.model.CityWeather;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.service.WeatherApiTransactionService;
import ru.khodov.springbootapp.util.EntityAlreadyExistsException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

@Service
@Qualifier("weatherApiJdbcTransactionServiceImpl")
public class WeatherApiJdbcTransactionServiceImpl implements WeatherApiTransactionService {
    private final DataSource dataSource;
    private final CityJdbcServiceImpl cityJdbcService;

    private final CityWeatherJdbcServiceImpl cityWeatherJdbcService;

    private final WeatherTypeJdbcServiceImpl weatherTypeJdbcService;

    private final Logger logger = LoggerFactory.getLogger(WeatherApiJdbcTransactionServiceImpl.class);


    public WeatherApiJdbcTransactionServiceImpl(DataSource dataSource, CityJdbcServiceImpl cityJdbcService,
                                                CityWeatherJdbcServiceImpl cityWeatherJdbcService, WeatherTypeJdbcServiceImpl weatherTypeJdbcService) {
        this.dataSource = dataSource;
        this.cityJdbcService = cityJdbcService;
        this.cityWeatherJdbcService = cityWeatherJdbcService;
        this.weatherTypeJdbcService = weatherTypeJdbcService;
    }

    public void addDataFromWeatherApi(WeatherApi weatherApi) {
        Connection conn = null;
        Savepoint savepoint = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            savepoint = conn.setSavepoint("savepoint");

            String name = weatherApi.location().name();

            if (cityJdbcService.countByName(name) > 0)
                throw new EntityAlreadyExistsException("City with name " + name + " already exists.");


            City city = new City();
            city.setName(name);
            City newCity = cityJdbcService.createCity(city);

            String type = weatherApi.current().condition().text();

            if (weatherTypeJdbcService.countByType(type) > 0)
                throw new EntityAlreadyExistsException("Weather type " + type + " already exists.");


            WeatherType weatherType = new WeatherType();
            weatherType.setType(type);

            WeatherType newWeatherType = weatherTypeJdbcService.createWeatherType(weatherType);

            CityWeather cityWeather = new CityWeather();
            cityWeather.setCity(newCity);
            cityWeather.setWeatherType(newWeatherType);
            cityWeather.setTemperature(weatherApi.current().temp_c());
            cityWeather.setDateTime(weatherApi.location().localtime());

            cityWeatherJdbcService.createCityWeather(cityWeather);

            conn.commit();


        } catch (EntityAlreadyExistsException e) {

            logger.warn(e.getMessage(), e);
            rollbackConnection(conn, savepoint);

        } catch (SQLException e) {

            logger.error("SQL Exception: {}", e.getMessage());
            rollbackConnection(conn, savepoint);

        } finally {
            closeConnection(conn);
        }

    }

    private void rollbackConnection(Connection conn, Savepoint savepoint) {
        if (conn != null && savepoint != null) {
            try {
                conn.rollback(savepoint);
            } catch (SQLException e) {
                logger.error("Failed to rollback transaction: {}", e.getMessage());
            }
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Failed to close connection: {}", e.getMessage());
            }
        }
    }


}
