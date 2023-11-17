package ru.khodov.springbootapp.controllers.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.khodov.springbootapp.SpringBootAppApplicationTests;
import ru.khodov.springbootapp.model.User;
import ru.khodov.springbootapp.repositories.UserJpaRepository;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class AuthenticationControllerTest extends SpringBootAppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userRepository;

    @BeforeEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registration_Test_status_isOk() throws Exception {
        User user = new User();
        user.setUsername("Nikita");
        user.setPassword("1wd23");

        String requestBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

    }

}