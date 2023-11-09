package ru.khodov.springbootapp.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.khodov.springbootapp.dto.UserDto;
import ru.khodov.springbootapp.model.User;
import ru.khodov.springbootapp.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping(value = "/registration")
    public UserDto registration(@RequestBody User user) {
        return userService.save(user);
    }
}
