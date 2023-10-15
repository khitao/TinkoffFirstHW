package ru.khodov.springbootapp.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.khodov.springbootapp.model.City;
import ru.khodov.springbootapp.model.CityWeather;
import ru.khodov.springbootapp.model.WeatherType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityWeatherRowMapper implements RowMapper<CityWeather> {
    @Override
    public CityWeather mapRow(ResultSet rs, int rowNum) throws SQLException {
        CityWeather cityWeather = new CityWeather();
        cityWeather.setId(rs.getLong("id"));
        cityWeather.setTemperature(rs.getDouble("temperature"));
        cityWeather.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        City city = new City();
        city.setId(rs.getLong("city_id"));
        city.setName(rs.getString("city_name"));
        cityWeather.setCity(city);
        WeatherType weatherType = new WeatherType();
        weatherType.setId(rs.getLong("weather_type_id"));
        weatherType.setType(rs.getString("weather_type"));
        cityWeather.setWeatherType(weatherType);
        return cityWeather;
    }
}

