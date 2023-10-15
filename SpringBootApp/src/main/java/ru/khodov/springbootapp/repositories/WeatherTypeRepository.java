package ru.khodov.springbootapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khodov.springbootapp.model.WeatherType;

public interface WeatherTypeRepository extends JpaRepository<WeatherType, Long> {
}