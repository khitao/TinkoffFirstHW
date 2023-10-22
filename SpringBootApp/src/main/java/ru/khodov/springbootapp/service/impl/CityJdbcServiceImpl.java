package ru.khodov.springbootapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.City;
import ru.khodov.springbootapp.repositories.CityJdbcRepository;
import ru.khodov.springbootapp.service.CityService;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Qualifier("jdbcCityService")
public class CityJdbcServiceImpl implements CityService {

    private final CityJdbcRepository cityJdbcRepository;

    @Override
    public List<City> getAllCities() {
        return cityJdbcRepository.getAllCities();
    }

    @Override
    public City getCityById(Long id) {
        return cityJdbcRepository.getCityById(id);
    }

    public Integer countByName(String name){
        return cityJdbcRepository.countByName(name);
    }
    @Override
    public City createCity(City city) {
        return cityJdbcRepository.createCity(city);
    }

    @Override
    public City updateCity(Long id, City updatedCity) {
        if (cityJdbcRepository.getCityById(id) == null) {
            throw new NoSuchElementException("No value present");
        }
        return cityJdbcRepository.updateCity(id, updatedCity);
    }

    @Override
    public void deleteCity(Long id) {
        cityJdbcRepository.deleteCity(id);
    }
}
