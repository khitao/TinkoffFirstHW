package ru.khodov.springbootapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.khodov.springbootapp.model.Condition;
import ru.khodov.springbootapp.model.Current;
import ru.khodov.springbootapp.model.Location;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.service.WeatherApiService;
import ru.khodov.springbootapp.service.WeatherApiTransactionService;
import ru.khodov.springbootapp.util.BadRequestToWeatherApiException;
import ru.khodov.springbootapp.util.ManyRequestsException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
@WebMvcTest(WeatherApiController.class)
public class WeatherApiControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherApiService weatherApiClient;

    @MockBean(name = "weatherApiJdbcTransactionServiceImpl")
    private WeatherApiTransactionService weatherApiTransactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String LOCATION = "Moscow";
    private static final LocalDateTime DATE_TIME = LocalDateTime.now();
    private static final double TEMPERATURE = 1.0;

    @Test
    void getCurrentWeather_Test() throws Exception {
        WeatherApi weather = getWeatherApi();

        given(weatherApiClient.getCurrentWeather(LOCATION)).willReturn(weather);

        WeatherApi weatherApi = weatherApiClient.getCurrentWeather(LOCATION);

        assertNotNull(weatherApi);

        when(weatherApiClient.getCurrentWeather(LOCATION)).thenReturn(weather);

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String request = mvcResult.getResponse().getContentAsString();

        assertThat(request).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(weather));

    }

    @Test
    void getCurrentWeather_Test_ManyRequestsException() throws Exception {

        given(weatherApiClient.getCurrentWeather(LOCATION))
                .willThrow(new ManyRequestsException("Слишком много запросов"));

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        this.mockMvc.perform(requestBuilder).andExpect(status().isTooManyRequests());

        verify(weatherApiClient).getCurrentWeather(LOCATION);
    }

    @Test
    void getCurrentWeather_Test_BadRequestToWeatherApiException() throws Exception {

        given(weatherApiClient.getCurrentWeather("jsdcbwkjdbc"))
                .willThrow(new BadRequestToWeatherApiException("BadRequestToWeatherApi", HttpStatus.NOT_FOUND));

        var requestBuilder = get("/weather_api/current/" + "jsdcbwkjdbc");

        this.mockMvc.perform(requestBuilder).andExpect(status().isNotFound());

        verify(weatherApiClient).getCurrentWeather("jsdcbwkjdbc");
    }

    private WeatherApi getWeatherApi() {
        WeatherApi weatherApi = WeatherApi.builder()
                .location(Location.builder()
                        .name(LOCATION)
                        .region(LOCATION)
                        .country("Russia")
                        .localtime(DATE_TIME)
                        .build())
                .current(Current.builder()
                        .last_updated(DATE_TIME)
                        .temp_c(TEMPERATURE)
                        .is_day(1)
                        .condition(Condition.builder().text("Good").build())
                        .wind_kph(1.0)
                        .pressure_mb(12)
                        .precip_mm(777)
                        .humidity(0)
                        .cloud(0)
                        .feelslike_c(0.0)
                        .vis_km(1.0)
                        .gust_kph(1.0)
                        .build())
                .build();

        return weatherApi;
    }
}
