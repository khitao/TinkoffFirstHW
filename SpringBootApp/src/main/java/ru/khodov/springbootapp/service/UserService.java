package ru.khodov.springbootapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.dto.UserDto;
import ru.khodov.springbootapp.model.User;
import ru.khodov.springbootapp.repositories.RoleJpaRepository;
import ru.khodov.springbootapp.repositories.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.khodov.springbootapp.util.DuplicateException;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDto save(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new DuplicateException("Пользователь с таким именем существует");

        user.setRoles(roleRepository.findByName("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new UserDto(user.getUsername(), user.getPassword());
    }

}
