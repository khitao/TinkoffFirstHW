package ru.khodov.springbootapp.config;

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
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.weatherapi.com/v1")
                .build();
    }

}
