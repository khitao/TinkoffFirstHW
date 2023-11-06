package ru.khodov.springbootapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khodov.springbootapp.model.Role;

import java.util.Set;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Set<Role> findByName(String roleName);
}