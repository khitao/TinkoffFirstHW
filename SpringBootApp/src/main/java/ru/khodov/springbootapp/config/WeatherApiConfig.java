package ru.khodov.springbootapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WeatherApiConfig {

    @Bean
    public RateLimiter rateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(3))
                .limitForPeriod(1)
                .timeoutDuration(Duration.ofSeconds(1))
                .build();

        return RateLimiter.of("weatherApi", config);
    }



    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public WebClient weatherApiClient() {
        return WebClient.builder()
                .baseUrl("https://api.weatherapi.com/v1")
                .build();
    }

}
