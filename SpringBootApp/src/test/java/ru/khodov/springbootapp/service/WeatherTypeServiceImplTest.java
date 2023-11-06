package ru.khodov.springbootapp.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.repositories.WeatherTypeRepository;

import java.util.List;


@SpringBootTest
@Sql(scripts = "/clean-up.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Testcontainers
public class WeatherTypeServiceImplTest {

    private static final String WEATHER_TYPE = "Sunny";

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("tinkoff")
            .withUsername("postgres")
            .withPassword("00130013");


    @Qualifier("jpaWeatherTypeService")
    @Autowired
    private WeatherTypeService weatherTypeService;

    @SpyBean
    private WeatherTypeRepository weatherTypeRepository;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

    @Test
    void getAllWeatherTypes_Test() {
        WeatherType weatherType = buildWeatherType(WEATHER_TYPE);

        int beforeAdd = weatherTypeService.getAllWeatherTypes().size();

        weatherTypeRepository.save(weatherType);

        int afterAdd = weatherTypeService.getAllWeatherTypes().size();

        Mockito.verify(weatherTypeRepository).save(weatherType);

        Assertions.assertEquals(beforeAdd + 1, afterAdd);
    }

    @Test
    void updateWeatherType_Test() {
        WeatherType weatherType = buildWeatherType(WEATHER_TYPE);

        Long id = weatherTypeRepository.save(weatherType).getId();

        WeatherType updatedWeatherType = buildWeatherType("WEATHER_TYPE");

        WeatherType result = weatherTypeService.updateWeatherType(id, updatedWeatherType);

        Mockito.verify(weatherTypeRepository).save(weatherType);
        Mockito.verify(weatherTypeRepository).findById(id);
        Mockito.verify(weatherTypeRepository).save(result);

        Assertions.assertEquals(result.getType(), updatedWeatherType.getType());
    }

    @Test
    void getWeatherTypeById() {
        WeatherType weatherType = buildWeatherType(WEATHER_TYPE);

        Long id = weatherTypeRepository.save(weatherType).getId();

        WeatherType result = weatherTypeService.getWeatherTypeById(id);

        Mockito.verify(weatherTypeRepository).save(weatherType);
        Mockito.verify(weatherTypeRepository).findById(id);

        Assertions.assertEquals(weatherType, result);
    }

    @Test
    void createWeatherType_Test() {
        WeatherType weatherType = buildWeatherType(WEATHER_TYPE);

        WeatherType result = weatherTypeService.createWeatherType(weatherType);

        List<WeatherType> weatherTypeList = weatherTypeRepository.findAll();

        Assertions.assertEquals(1, weatherTypeList.size());


        Mockito.verify(weatherTypeRepository).save(weatherType);

        Assertions.assertEquals(weatherType, result);
    }

    WeatherType buildWeatherType(String type) {
        WeatherType weatherType = new WeatherType();
        weatherType.setType(type);

        return weatherType;
    }

}
