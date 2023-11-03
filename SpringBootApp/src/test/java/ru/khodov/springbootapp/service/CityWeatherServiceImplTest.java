package ru.khodov.springbootapp.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import ru.khodov.springbootapp.AbstractSingletonPostgresContainer;
import ru.khodov.springbootapp.model.City;
import ru.khodov.springbootapp.model.CityWeather;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.repositories.CityRepository;
import ru.khodov.springbootapp.repositories.CityWeatherRepository;
import ru.khodov.springbootapp.repositories.WeatherTypeRepository;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@Sql(scripts = "/clean-up.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CityWeatherServiceImplTest  extends AbstractSingletonPostgresContainer {


    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2000, 1, 3, 20, 20, 20);

    private static final LocalDateTime NEW_LOCAL_DATE_TIME = LocalDateTime.of(2020, 1, 6, 20, 10, 20);


    @Qualifier("jpaCityWeatherService")
    @Autowired
    private CityWeatherService cityWeatherService;

    @SpyBean
    private CityWeatherRepository cityWeatherRepository;

    @SpyBean
    private CityRepository cityRepository;

    @SpyBean
    private WeatherTypeRepository weatherTypeRepository;

    @Test
    void getAllCityWeather_Test() {
        CityWeather cityWeather = buildCityWeather(buildCity(), buildWeatherType(), 1.0, LOCAL_DATE_TIME);

        int beforeAdd = cityWeatherService.getAllCityWeather().size();

        cityWeatherRepository.save(cityWeather);

        int afterAdd = cityWeatherService.getAllCityWeather().size();

        Mockito.verify(cityWeatherRepository).save(cityWeather);

        Assertions.assertEquals(beforeAdd + 1, afterAdd);
    }

    @Test
    void getCityWeatherById_Test() {
        CityWeather cityWeather = buildCityWeather(buildCity(), buildWeatherType(), 1.0, LOCAL_DATE_TIME);

        Long id = cityWeatherRepository.save(cityWeather).getId();

        CityWeather result = cityWeatherService.getCityWeatherById(id);

        Mockito.verify(cityWeatherRepository).save(cityWeather);
        Mockito.verify(cityWeatherRepository).findById(id);

        Assertions.assertEquals(cityWeather, result);
    }

    @Test
    void createCityWeather_Test() {
        CityWeather cityWeather = buildCityWeather(buildCity(), buildWeatherType(), 1.0, LOCAL_DATE_TIME);

        CityWeather result = cityWeatherService.createCityWeather(cityWeather);

        List<CityWeather> cityWeathers = cityWeatherRepository.findAll();

        Assertions.assertEquals(1, cityWeathers.size());

        Mockito.verify(cityWeatherRepository).save(cityWeather);

        Assertions.assertEquals(cityWeather, result);
    }

    @Test
    void updateCityWeather_Test() {
        City city = buildCity();
        WeatherType weatherType = buildWeatherType();
        CityWeather cityWeather = buildCityWeather(city, weatherType, 1.0, LOCAL_DATE_TIME);

        Long id = cityWeatherRepository.save(cityWeather).getId();

        CityWeather updateCityWeather = buildCityWeather(city, weatherType, 15.0, NEW_LOCAL_DATE_TIME);

        CityWeather result = cityWeatherService.updateCityWeather(id, updateCityWeather);

        Mockito.verify(cityWeatherRepository).save(cityWeather);
        Mockito.verify(cityWeatherRepository).findById(id);
        Mockito.verify(cityWeatherRepository).save(result);

        assertEquals(updateCityWeather, result);
    }

    @Test
    void deleteCityWeather_Test() {
        CityWeather cityWeather = buildCityWeather(buildCity(), buildWeatherType(), 1.0, LOCAL_DATE_TIME);

        Long id = cityWeatherRepository.save(cityWeather).getId();

        List<CityWeather> beforeDelete = cityWeatherRepository.findAll();
        int beforeSize = beforeDelete.size();

        cityWeatherService.deleteCityWeather(id);

        List<CityWeather> afterDelete = cityWeatherRepository.findAll();
        int afterSize = afterDelete.size();

        Mockito.verify(cityWeatherRepository).save(cityWeather);
        Mockito.verify(cityWeatherRepository).deleteById(id);

        Assertions.assertEquals(beforeSize - 1, afterSize);
    }


    void assertEquals(CityWeather updateCityWeather, CityWeather result) {
        Assertions.assertEquals(result.getCity(), updateCityWeather.getCity());
        Assertions.assertEquals(result.getWeatherType(), updateCityWeather.getWeatherType());
        Assertions.assertEquals(result.getTemperature(), updateCityWeather.getTemperature());
        Assertions.assertEquals(result.getDateTime(), updateCityWeather.getDateTime());
    }

    CityWeather buildCityWeather(City city, WeatherType weatherType, Double temperature, LocalDateTime dateTime) {
        CityWeather cityWeather = new CityWeather();
        cityWeather.setWeatherType(weatherType);
        cityWeather.setCity(city);
        cityWeather.setTemperature(temperature);
        cityWeather.setDateTime(dateTime);

        return cityWeather;
    }

    WeatherType buildWeatherType() {
        WeatherType weatherType = new WeatherType();
        weatherType.setType("Sunny");
        Long id = weatherTypeRepository.save(weatherType).getId();

        weatherType.setId(id);

        return weatherType;
    }

    City buildCity() {
        City city = new City();
        city.setName("Moscow");
        Long id = cityRepository.save(city).getId();
        city.setId(id);

        return city;
    }
}
