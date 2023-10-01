package ru.khodov.springbootapp.service;


import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.Weather;
import ru.khodov.springbootapp.util.DeleteException;
import ru.khodov.springbootapp.util.DuplicateRegionException;
import ru.khodov.springbootapp.util.RegionNotFoundException;
import ru.khodov.springbootapp.util.SuccessCreateException;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class WeatherService {

    private static Map<String, ArrayList<Weather>> weatherData = new HashMap<>();


    public double getRegionTemperatureForTheCurrentDate(@NonNull String regionName, @NonNull LocalDateTime dateTime) {
        String regionNameWithoutSpaces = regionName.replace(" ", "");

        if (weatherData.containsKey(regionNameWithoutSpaces)) {
            return weatherData.get(regionNameWithoutSpaces)
                    .stream()
                    .filter(weather -> weather.getDateTime().equals(dateTime))
                    .mapToDouble(Weather::getTemperature)
                    .findAny()
                    .orElseThrow(() -> new RegionNotFoundException("Региона с такими данными нет"));
        } else {
            throw new RegionNotFoundException("Такого региона не существует");
        }

    }


    public void addNewRegion(@NonNull String regionName, @NonNull Weather weather) {
        String regionNameWithoutSpaces = regionName.replace(" ", "");

        if (!weatherData.containsKey(regionNameWithoutSpaces)) {
            weatherData.put(regionNameWithoutSpaces, new ArrayList<>(Arrays.asList(weather)));
            throw new SuccessCreateException("Регион успешно добавлен");
        } else {
            throw new DuplicateRegionException("Регион с таким именем уже существует");
        }

    }

    public void updateTemperatureInRegion(@NonNull String regionName, @NonNull LocalDateTime dateTime, double temperature) {
        String regionNameWithoutSpaces = regionName.replace(" ", "");

        if (weatherData.containsKey(regionNameWithoutSpaces)) {

            Weather weather = weatherData.get(regionNameWithoutSpaces).stream()
                    .filter(w -> w.getDateTime().equals(dateTime)).findAny().orElse(null);

            if (weather != null) {
                weather.setTemperature(temperature);
            } else {
                List<Weather> regionWeather = weatherData.computeIfAbsent(regionNameWithoutSpaces, key -> new ArrayList<>());
                regionWeather.add(new Weather(regionNameWithoutSpaces, temperature, dateTime));

                throw new RegionNotFoundException("Региона с данными параметрами нет, но мы его добавили");
            }

        } else {
            weatherData.put(regionNameWithoutSpaces, new ArrayList<>(Arrays.asList(new Weather(regionNameWithoutSpaces, temperature, dateTime))));
            throw new RegionNotFoundException("Региона с данными параметрами нет, но мы его добавили");
        }
    }

    public void deleteRegion(@NonNull String regionName) {
        String regionNameWithoutSpaces = regionName.replace(" ", "");

        if (weatherData.containsKey(regionNameWithoutSpaces)) {
            weatherData.remove(regionNameWithoutSpaces);
            throw new DeleteException("Регион успешно удалён");
        } else {
            throw new RegionNotFoundException("Региона с таким именем нет. Мы не можем его удалить.");
        }
    }


}
