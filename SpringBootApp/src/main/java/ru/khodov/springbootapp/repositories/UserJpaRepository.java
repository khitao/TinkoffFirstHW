package ru.khodov.springbootapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khodov.springbootapp.model.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
