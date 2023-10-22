package ru.khodov.springbootapp.repositories;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.khodov.springbootapp.mapper.CityRowMapper;
import ru.khodov.springbootapp.model.City;

import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CityJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<City> getAllCities() {
        return jdbcTemplate.query("SELECT * FROM city",  new CityRowMapper());
    }

    public City getCityById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM city WHERE id = ?", new CityRowMapper(), id);
    }

    public Integer countByName(String name) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM city WHERE name = ?", Integer.class, name);
        return count;
    }

    public City createCity(City city) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO city (name) VALUES (?)", new String[]{"id"});
            ps.setString(1, city.getName());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        city.setId(generatedId);

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
