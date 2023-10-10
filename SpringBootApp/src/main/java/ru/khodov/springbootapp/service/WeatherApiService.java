package ru.khodov.springbootapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.util.ManyRequestsException;
import ru.khodov.springbootapp.util.WeatherApiError;
import ru.khodov.springbootapp.util.BadRequestToWeatherApiException;


@Service
public class WeatherApiService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final WebClient weatherApiClient;
    private final RateLimiter rateLimiter;
    private final ObjectMapper objectMapper;

    public WeatherApiService(@Qualifier("weatherApiClient") WebClient weatherApiClient, RateLimiter rateLimiter, ObjectMapper objectMapper) {
        this.weatherApiClient = weatherApiClient;
          this.rateLimiter = rateLimiter;
        this.objectMapper = objectMapper;
    }


    public Mono<ResponseEntity<WeatherApi>> getCurrentWeather(String location) {

        boolean permission = rateLimiter.acquirePermission();

        if (!permission) {
            throw new ManyRequestsException("Слишком много запросов");
        }


        return weatherApiClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/current.json")
                        .queryParam("key", apiKey)
                        .queryParam("q", location)
                        .build())
                .retrieve()
                .bodyToMono(WeatherApi.class)
                .map(weatherApi -> new ResponseEntity<>(weatherApi, HttpStatus.OK))
                .onErrorResume(WebClientResponseException.class, e -> Mono.just(handleWebClientResponseException(e)));
    }

    private ResponseEntity<WeatherApi> handleWebClientResponseException(WebClientResponseException e) {

        try {
            String errorResponseBody = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            WeatherApiError weatherApiError = objectMapper.readValue(errorResponseBody, WeatherApiError.class);

            int errorCode = weatherApiError.getError().getCode();
            String errorMessage = weatherApiError.getError().getMessage();

            throw new BadRequestToWeatherApiException("Ошибка: " + statusCode + ", Код ошибки API: " + errorCode + ", Описание ошибки API: " + errorMessage, statusCode);

        } catch (JsonProcessingException jsonException) {

            throw new RuntimeException("Ошибка при парсинге JSON");

        }
    }

}

