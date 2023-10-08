package ru.khodov.springbootapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.util.WeatherApiError;
import ru.khodov.springbootapp.util.BadRequestToWeatherApiException;
import ru.khodov.springbootapp.util.ManyRequestsException;


@Service
public class WeatherApiService {

    @Value("${weather.api.key}")
    private String apiKey;

    WebClient webClient;
    private final RateLimiter rateLimiter;

    public WeatherApiService(@Qualifier("webClient") WebClient webClient, RateLimiter rateLimiter) {
        this.webClient = webClient;
        this.rateLimiter = rateLimiter;
    }

    public ResponseEntity<WeatherApi> getCurrentWeather(String location) {

        boolean permission = rateLimiter.acquirePermission();

        if (!permission) {
            throw new ManyRequestsException("Слишком много запросов");
        }

        try {
            WeatherApi weatherResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/current.json")
                            .queryParam("key", apiKey)
                            .queryParam("q", location)
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherApi.class)
                    .block();

            return new ResponseEntity<>(weatherResponse, HttpStatus.OK);

        } catch (WebClientResponseException e) {

            try {
                String errorResponseBody = e.getResponseBodyAsString();
                HttpStatusCode statusCode = e.getStatusCode();

                ObjectMapper objectMapper = new ObjectMapper();
                WeatherApiError weatherApiError = objectMapper.readValue(errorResponseBody, WeatherApiError.class);

                int errorCode = weatherApiError.getError().getCode();
                String errorMessage = weatherApiError.getError().getMessage();

                throw new BadRequestToWeatherApiException("Ошибка: " + statusCode + ", Код ошибки API: " + errorCode + ", Описание ошибки API: " + errorMessage, statusCode);

            } catch (JsonProcessingException jsonException) {

                throw new RuntimeException("Ошибка при парсинге JSON");

            }


        }

    }


}
