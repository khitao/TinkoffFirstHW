package ru.khodov.springbootapp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;

import ru.khodov.springbootapp.dto.WeatherDto;
import ru.khodov.springbootapp.dto.WeatherRequestDto;

import ru.khodov.springbootapp.service.WeatherService;
import ru.khodov.springbootapp.util.DuplicateException;
import ru.khodov.springbootapp.util.RegionNotFoundException;

import java.time.LocalDateTime;


import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(printOnlyOnFailure = false, addFilters = false)
@WebMvcTest(WeatherController.class)
public class WeatherControllerIntegrationTest {


    private static final String REGION_NAME = "Moscow";
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2023, 10, 30, 10, 9, 21);
    private static final double TEMPERATURE = 1.0;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getTemperatureByRegionNameAndDate_Test() throws Exception {

        given(weatherService.getRegionTemperatureForTheCurrentDate(REGION_NAME, DATE_TIME)).willReturn(TEMPERATURE);

        var requestBuilder = get("/api/weather/" + REGION_NAME).param("dateTime", String.valueOf(DATE_TIME));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().string(String.valueOf(TEMPERATURE))
                );

        verify(weatherService).getRegionTemperatureForTheCurrentDate(REGION_NAME, DATE_TIME);
    }

    @Test
    void getTemperatureByRegionNameAndDate_Test_RegionNotFoundException() throws Exception {

        given(weatherService.getRegionTemperatureForTheCurrentDate(REGION_NAME, DATE_TIME)).willThrow(new RegionNotFoundException("Такого региона не существует"));

        var requestBuilder = get("/api/weather/" + REGION_NAME).param("dateTime", String.valueOf(DATE_TIME));


        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        content().string("Такого региона не существует"));

        verify(weatherService).getRegionTemperatureForTheCurrentDate(REGION_NAME, DATE_TIME);
    }

    @Test
    void addNewRegion_Test() throws Exception {

        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(TEMPERATURE, DATE_TIME);
        WeatherDto weatherDto = new WeatherDto(REGION_NAME, TEMPERATURE);

        String requestBody = objectMapper.writeValueAsString(weatherRequestDto);

        given(weatherService.addNewRegion(REGION_NAME, weatherRequestDto)).willReturn(weatherDto);

        var requestBuilder = post("/api/weather/" + REGION_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.regionName").value(REGION_NAME))
                .andExpect(jsonPath("$.temperature").value(TEMPERATURE))
                .andReturn();


        assertThat("application/json").isEqualTo(mvcResult.getResponse().getContentType());

        verify(weatherService).addNewRegion(REGION_NAME, weatherRequestDto);
    }

    @Test
    void addNewRegion_Test_DuplicateRegionException() throws Exception {

        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(TEMPERATURE, DATE_TIME);

        String requestBody = objectMapper.writeValueAsString(weatherRequestDto);

        given(weatherService.addNewRegion(REGION_NAME, weatherRequestDto)).willThrow(new DuplicateException("Регион с таким именем уже существует"));

        var requestBuilder = post("/api/weather/" + REGION_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        this.mockMvc.perform(requestBuilder).andExpect(status().isConflict());

        verify(weatherService).addNewRegion(REGION_NAME, weatherRequestDto);
    }

}
