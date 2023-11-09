package ru.khodov.springbootapp.controllers.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.khodov.springbootapp.dto.UpdateRegionTemperatureDto;
import ru.khodov.springbootapp.dto.WeatherRequestDto;


import java.time.LocalDateTime;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class WeatherControllerTest {

    private static final String REGION_NAME = "Moscow";
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2023, 10, 30, 10, 9, 21);
    private static final double TEMPERATURE = 1.0;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTemperatureByRegionNameAndDate_Test_status_isUnauthorized() throws Exception {

        var requestBuilder = get("/api/weather/" + REGION_NAME).param("dateTime", String.valueOf(DATE_TIME));

        this.mockMvc.perform(requestBuilder)
                .andExpect(
                        status().isUnauthorized());

    }

    @Test
    @WithMockUser(roles = "USER")
    void getTemperatureByRegionNameAndDate_Test_status_isOk_USER() throws Exception {

        var requestBuilder = get("/api/weather/" + REGION_NAME).param("dateTime", String.valueOf(DATE_TIME));

        this.mockMvc.perform(requestBuilder)
                .andExpect(
                        status().isOk());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTemperatureByRegionNameAndDate_Test_status_isOk_ADMIN() throws Exception {

        var requestBuilder = get("/api/weather/" + REGION_NAME).param("dateTime", String.valueOf(DATE_TIME));

        this.mockMvc.perform(requestBuilder)
                .andExpect(
                        status().isOk());

    }

    @Test
    void addNewRegion_Test_status_isUnauthorized() throws Exception {

        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(TEMPERATURE, DATE_TIME);

        String requestBody = objectMapper.writeValueAsString(weatherRequestDto);

        var requestBuilder = post("/api/weather/" + REGION_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addNewRegion_Test_status_isCreated_ADMIN() throws Exception {

        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(TEMPERATURE, DATE_TIME);

        String requestBody = objectMapper.writeValueAsString(weatherRequestDto);

        var requestBuilder = post("/api/weather/" + REGION_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(roles = "USER")
    void addNewRegion_Test_status_isInternalServerError_USER() throws Exception {

        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(TEMPERATURE, DATE_TIME);

        String requestBody = objectMapper.writeValueAsString(weatherRequestDto);

        var requestBuilder = post("/api/weather/" + REGION_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError());

    }

    @Test
    void updateRegionTemperature_Test_status_isUnauthorized() throws Exception {
        UpdateRegionTemperatureDto updateRegionTemperatureDto = new UpdateRegionTemperatureDto(DATE_TIME, TEMPERATURE);

        String requestBody = objectMapper.writeValueAsString(updateRegionTemperatureDto);

        var requestBuilder = put("/api/weather/" + REGION_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRegionTemperature_Test_status_isOk_ADMIN() throws Exception {
        UpdateRegionTemperatureDto updateRegionTemperatureDto = new UpdateRegionTemperatureDto(DATE_TIME, TEMPERATURE);

        String requestBody = objectMapper.writeValueAsString(updateRegionTemperatureDto);

        var requestBuilder = put("/api/weather/" + REGION_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);


        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void deleteRegion_Test_status_isUnauthorized() throws Exception {
        var requestBuilder = delete("/api/weather/" + REGION_NAME);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRegion_Test_status_isNotFound_ADMIN() throws Exception {

        var requestBuilder = delete("/api/weather/" + REGION_NAME);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteRegion_Test_status_isInternalServerError_USER() throws Exception {

        var requestBuilder = delete("/api/weather/" + REGION_NAME);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError());
    }
}

