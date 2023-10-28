package ru.khodov.springbootapp.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.khodov.springbootapp.mapper.WeatherTypeRowMapper;
import ru.khodov.springbootapp.model.WeatherType;

import java.sql.PreparedStatement;
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO weather_type (type) VALUES (?)", new String[]{"id"});
            ps.setString(1, weatherType.getType());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        weatherType.setId(generatedId);

        return weatherType;
    }

    public Integer countByType(String type) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM weather_type WHERE type = ?", Integer.class, type);
        return count;
    }

    public WeatherType updateWeatherType(Long id, WeatherType updatedWeatherType) {
        jdbcTemplate.update("UPDATE weather_type SET name = ? WHERE id = ?", updatedWeatherType.getType(), id);
        return updatedWeatherType;
    }

}
