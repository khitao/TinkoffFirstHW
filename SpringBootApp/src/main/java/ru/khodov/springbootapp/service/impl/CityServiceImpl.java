package ru.khodov.springbootapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.City;
import ru.khodov.springbootapp.repositories.CityRepository;
import ru.khodov.springbootapp.service.CityService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Qualifier("jpaCityService")
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id).orElseThrow();
    }

    public Integer countByName(String name){
        return cityRepository.countByName(name);
    }

    public City createCity(City city) {
        return cityRepository.save(city);
    }

    public City updateCity(Long id, City updatedCity) {
        City existingCity = cityRepository.findById(id).orElseThrow();
        existingCity.setName(updatedCity.getName());
        return cityRepository.save(existingCity);
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
