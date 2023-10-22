package ru.khodov.springbootapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.khodov.springbootapp.model.CityWeather;



public interface CityWeatherRepository extends JpaRepository<CityWeather, Long> {
}
