package ru.khodov.springbootapp.service;



import ru.khodov.springbootapp.model.City;

import java.util.List;

public interface CityService {
    List<City> getAllCities();

    City getCityById(Long id);

    City createCity(City city);

    City updateCity(Long id, City updatedCity);

    void deleteCity(Long id);
}
