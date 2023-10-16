package ru.khodov.springbootapp.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.khodov.springbootapp.model.City;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityRowMapper implements RowMapper<City> {
    @Override
    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getLong("id"));
        city.setName(rs.getString("name"));
        return city;
    }
}
