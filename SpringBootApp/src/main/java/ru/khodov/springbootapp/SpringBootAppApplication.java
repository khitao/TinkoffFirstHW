package ru.khodov.springbootapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.service.WeatherApiJdbcTransactionService;
import ru.khodov.springbootapp.service.WeatherApiJpaTransactionService;
import ru.khodov.springbootapp.service.WeatherApiService;

@SpringBootApplication
public class SpringBootAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(WeatherApiJdbcTransactionService weatherApiJdbcTransactionService,
                                               WeatherApiJpaTransactionService weatherApiJpaTransactionService,
                                               WeatherApiService weatherApiService) {
        return args -> {

            WeatherApi weatherApi = weatherApiService.getCurrentWeather("London").block();

            weatherApiJdbcTransactionService.addDataFromWeatherApi(weatherApi);

            weatherApi = weatherApiService.getCurrentWeather("London").block();

            weatherApiJpaTransactionService.addDataFromWeatherApi(weatherApi);

        };
    }

}
