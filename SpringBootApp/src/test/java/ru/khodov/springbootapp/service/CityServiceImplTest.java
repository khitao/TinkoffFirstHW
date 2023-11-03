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
import ru.khodov.springbootapp.repositories.CityRepository;


import java.util.List;


@SpringBootTest
@Sql(scripts = "/clean-up.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CityServiceImplTest extends AbstractSingletonPostgresContainer {


    private static final String CITY_NAME = "Moscow";


    @Qualifier("jpaCityService")
    @Autowired
    private CityService cityService;

    @SpyBean
    private CityRepository cityRepository;



    @Test
    void getAllCities_Test() {
        City city = buildCity(CITY_NAME);

        cityRepository.save(city);

        City newCity = buildCity("NewName");

        cityRepository.save(newCity);

        List<City> cityList = cityService.getAllCities();

        Mockito.verify(cityRepository).findAll();

        Assertions.assertEquals(2, cityList.size());
    }

    @Test
    void getCityById_Test() {
        City city = buildCity(CITY_NAME);

        Long id = cityRepository.save(city).getId();

        City result = cityService.getCityById(id);

        Mockito.verify(cityRepository).save(city);
        Mockito.verify(cityRepository).findById(id);

        Assertions.assertEquals(city, result);
    }

    @Test
    void createCity_Test() {
        City city = buildCity(CITY_NAME);

        City result = cityService.createCity(city);

        List<City> cityList = cityRepository.findAll();

        Assertions.assertEquals(1, cityList.size());

        Mockito.verify(cityRepository).save(city);

        Assertions.assertEquals(city, result);
    }

    @Test
    void updateCity_Test() {
        City city = buildCity(CITY_NAME);

        Long id = cityRepository.save(city).getId();

        City newCity = buildCity("CITY_NAME");

        City result = cityService.updateCity(id, newCity);

        Mockito.verify(cityRepository).save(city);
        Mockito.verify(cityRepository).save(result);

        Assertions.assertEquals(newCity.getName(), result.getName());
    }

    @Test
    void deleteCity_Test() {
        City city = buildCity(CITY_NAME);

        Long id = cityRepository.save(city).getId();

        List<City> beforeDelete = cityRepository.findAll();
        int beforeSize = beforeDelete.size();

        cityService.deleteCity(id);

        List<City> afterDelete = cityRepository.findAll();
        int afterSize = afterDelete.size();

        Mockito.verify(cityRepository).save(city);
        Mockito.verify(cityRepository).deleteById(id);

        Assertions.assertEquals(beforeSize - 1, afterSize);
    }

    City buildCity(String name) {
        City city = new City();
        city.setName(name);

        return city;
    }
}
