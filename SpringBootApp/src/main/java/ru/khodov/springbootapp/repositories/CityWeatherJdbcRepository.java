package ru.khodov.springbootapp.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.khodov.springbootapp.mapper.CityWeatherRowMapper;
import ru.khodov.springbootapp.model.CityWeather;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CityWeatherJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<CityWeather> getAllCityWeather() {
        String sql = """
                SELECT cw.id, cw.temperature, cw.date_time, c.id as city_id, c.name as city_name, wt.id as weather_type_id, wt.type as weather_type 
                FROM city_weather cw 
                LEFT JOIN city c ON cw.city_id = c.id 
                LEFT JOIN weather_type wt ON cw.weather_type_id = wt.id
                """;
        return jdbcTemplate.query(sql, new CityWeatherRowMapper());
    }

    public CityWeather getCityWeatherById(Long id) {
        String sql = """
    SELECT cw.id, cw.temperature, cw.date_time, 
           c.id AS city_id, c.name AS city_name, 
           wt.id AS weather_type_id, wt.type AS weather_type 
    FROM city_weather cw 
    LEFT JOIN city c ON cw.city_id = c.id 
    LEFT JOIN weather_type wt ON cw.weather_type_id = wt.id 
    WHERE cw.id = ?
    """;

        return jdbcTemplate.queryForObject(sql, new CityWeatherRowMapper(), id);
    }


    public CityWeather createCityWeather(CityWeather cityWeather) {
        jdbcTemplate.update("INSERT INTO city_weather (date_time, temperature, city_id, weather_type_id) VALUES (?, ?, ?, ?)",
                cityWeather.getDateTime(), cityWeather.getTemperature(), cityWeather.getCity().getId(), cityWeather.getWeatherType().getId());
        return cityWeather;
    }

    public CityWeather updateCityWeather(Long id, CityWeather updatedCityWeather) {
        jdbcTemplate.update("UPDATE city_weather SET date_time = ?,  temperature = ?, city_id = ?, weather_type_id = ? WHERE id = ?",
                updatedCityWeather.getDateTime(), updatedCityWeather.getTemperature(),
                updatedCityWeather.getCity().getId(), updatedCityWeather.getWeatherType().getId(), id);
        return updatedCityWeather;
    }

    public void deleteCityWeather(Long id) {
        jdbcTemplate.update("DELETE FROM city_weather WHERE id = ?", id);
    }
}

