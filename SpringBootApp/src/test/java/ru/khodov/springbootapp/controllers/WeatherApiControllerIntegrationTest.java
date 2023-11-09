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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(printOnlyOnFailure = false, addFilters = false)
@WebMvcTest(WeatherApiController.class)
public class WeatherApiControllerIntegrationTest {

    private static final String LOCATION = "Moscow";
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2023, 7, 20, 12, 20);
    private static final double TEMPERATURE = 1.0;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherApiService weatherApiClient;

    @MockBean(name = "weatherApiJdbcTransactionServiceImpl")
    private WeatherApiTransactionService weatherApiTransactionService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getCurrentWeather_Test() throws Exception {
        WeatherApi weather = getWeatherApi();

        given(weatherApiClient.getCurrentWeather(LOCATION)).willReturn(weather);

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.name").value(LOCATION))
                .andExpect(jsonPath("$.location.region").value(LOCATION))
                .andExpect(jsonPath("$.location.country").value("Russia"))
                .andExpect(jsonPath("$.location.localtime").value("2023-07-20 12:20"))
                .andExpect(jsonPath("$.current.last_updated").value("2023-07-20 12:20"))
                .andExpect(jsonPath("$.current.temp_c").value(TEMPERATURE))
                .andExpect(jsonPath("$.current.is_day").value(1))
                .andExpect(jsonPath("$.current.condition.text").value("Good"))
                .andExpect(jsonPath("$.current.wind_kph").value(1.0))
                .andExpect(jsonPath("$.current.pressure_mb").value(12))
                .andExpect(jsonPath("$.current.precip_mm").value(777))
                .andExpect(jsonPath("$.current.humidity").value(0))
                .andExpect(jsonPath("$.current.cloud").value(0))
                .andExpect(jsonPath("$.current.feelslike_c").value(0.0))
                .andExpect(jsonPath("$.current.vis_km").value(1.0))
                .andExpect(jsonPath("$.current.gust_kph").value(1.0))
                .andReturn();

        assertThat("application/json").isEqualTo(mvcResult.getResponse().getContentType());

        verify(weatherApiClient).getCurrentWeather(LOCATION);

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
