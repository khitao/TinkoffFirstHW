package ru.khodov.springbootapp.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.khodov.springbootapp.mapper.WeatherTypeRowMapper;
import ru.khodov.springbootapp.model.WeatherType;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class WeatherTypeJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<WeatherType> getAllWeatherTypes() {
        return jdbcTemplate.query("SELECT * FROM weather_type", new WeatherTypeRowMapper());
    }

    public WeatherType getWeatherTypeById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM weather_type WHERE id = ?", new WeatherTypeRowMapper(), id);
    }

    public WeatherType createWeatherType(WeatherType weatherType) {
        jdbcTemplate.update("INSERT INTO weather_type (name) VALUES (?)", weatherType.getType());
        return weatherType;
    }

    public WeatherType updateWeatherType(Long id, WeatherType updatedWeatherType) {
        jdbcTemplate.update("UPDATE weather_type SET name = ? WHERE id = ?", updatedWeatherType.getType(), id);
        return updatedWeatherType;
    }

}
