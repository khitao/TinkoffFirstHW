package ru.khodov.springbootapp.controllers.security;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.khodov.springbootapp.service.WeatherApiTransactionService;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class WeatherApiControllerTest {

    private static final String LOCATION = "Moscow";

    @Autowired
    private MockMvc mockMvc;


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

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCurrentWeather_Test_status_isOk_ADMIN() throws Exception {

        var requestBuilder = get("/weather_api/current/" + LOCATION);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}
