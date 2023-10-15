package ru.khodov.springbootapp.repositories;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.khodov.springbootapp.model.City;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CityJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<City> getAllCities() {
        return jdbcTemplate.query("SELECT * FROM city", new BeanPropertyRowMapper<>(City.class));
    }

    public City getCityById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM city WHERE id = ?", new BeanPropertyRowMapper<>(City.class), id);
    }

    public City createCity(City city) {
        jdbcTemplate.update("INSERT INTO city (name) VALUES (?)", city.getName());
        return city;
    }

    public City updateCity(Long id, City updatedCity) {
        jdbcTemplate.update("UPDATE city SET name = ? WHERE id = ?", updatedCity.getName(), id);
        return updatedCity;
    }

    public void deleteCity(Long id) {
        jdbcTemplate.update("DELETE FROM city WHERE id = ?", id);
    }
}
