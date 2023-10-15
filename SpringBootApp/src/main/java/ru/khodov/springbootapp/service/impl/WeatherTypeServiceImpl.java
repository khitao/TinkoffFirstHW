package ru.khodov.springbootapp.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.WeatherType;
import ru.khodov.springbootapp.repositories.WeatherTypeRepository;
import ru.khodov.springbootapp.service.WeatherTypeService;


import java.util.List;

@RequiredArgsConstructor
@Service
@Qualifier("jpaWeatherTypeService")
public class WeatherTypeServiceImpl implements WeatherTypeService {

    private final WeatherTypeRepository weatherTypeRepository;

    public List<WeatherType> getAllWeatherTypes() {
        return weatherTypeRepository.findAll();
    }

    public WeatherType getWeatherTypeById(Long id) {
        return weatherTypeRepository.findById(id).orElseThrow();
    }

    public WeatherType createWeatherType(WeatherType weatherType) {
        return weatherTypeRepository.save(weatherType);
    }

    public WeatherType updateWeatherType(Long id, WeatherType updatedWeatherType) {
        WeatherType existingWeatherType = weatherTypeRepository.findById(id).orElseThrow();

        existingWeatherType.setType(updatedWeatherType.getType());
        return weatherTypeRepository.save(existingWeatherType);
    }

}
