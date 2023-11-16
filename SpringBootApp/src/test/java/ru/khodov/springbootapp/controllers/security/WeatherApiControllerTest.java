package ru.khodov.springbootapp.controllers.security;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.khodov.springbootapp.SpringBootAppApplicationTests;
import ru.khodov.springbootapp.model.Condition;
import ru.khodov.springbootapp.model.Current;
import ru.khodov.springbootapp.model.Location;
import ru.khodov.springbootapp.model.WeatherApi;
import ru.khodov.springbootapp.service.WeatherApiService;
import ru.khodov.springbootapp.service.WeatherApiTransactionService;


import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
public class WeatherApiControllerTest extends SpringBootAppApplicationTests {

    private static final String LOCATION = "Moscow";
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2023, 7, 20, 12, 20);
    private static final double TEMPERATURE = 1.0;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherApiService weatherApiClient;


    @MockBean(name = "weatherApiJdbcTransactionServiceImpl")
    private WeatherApiTransactionService weatherApiTransactionService;


    @Test
    void getCurrentWeather_Test_status_isUnauthorized() throws Exception {

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(roles = "USER")
    void getCurrentWeather_Test_status_isOk_USER() throws Exception {

        WeatherApi weather = getWeatherApi();

        given(weatherApiClient.getCurrentWeather(LOCATION)).willReturn(weather);

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCurrentWeather_Test_status_isOk_ADMIN() throws Exception {

        WeatherApi weather = getWeatherApi();

        given(weatherApiClient.getCurrentWeather(LOCATION)).willReturn(weather);

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
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
