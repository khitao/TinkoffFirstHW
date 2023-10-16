package ru.khodov.springbootapp.mapper;


import org.springframework.jdbc.core.RowMapper;
import ru.khodov.springbootapp.model.WeatherType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WeatherTypeRowMapper implements RowMapper<WeatherType> {
    @Override
    public WeatherType mapRow(ResultSet rs, int rowNum) throws SQLException {
        WeatherType weatherType = new WeatherType();
        weatherType.setId(rs.getLong("id"));
        weatherType.setType(rs.getString("type"));
        return weatherType;
    }
}
