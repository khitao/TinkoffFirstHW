package ru.khodov.springbootapp.service;


import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.model.Weather;
import ru.khodov.springbootapp.util.DeleteException;
import ru.khodov.springbootapp.util.DuplicateRegionException;
import ru.khodov.springbootapp.util.RegionNotFoundException;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class WeatherService {

    private static Map<String, ArrayList<Weather>> weatherData = new HashMap<>();


    public double getRegionTemperatureForTheCurrentDate(@NonNull String regionName, @NonNull LocalDateTime dateTime) {

        if (weatherData.containsKey(regionName)) {
            return weatherData.get(regionName)
                    .stream()
                    .filter(weather -> weather.getDateTime().equals(dateTime))
                    .mapToDouble(Weather::getTemperature)
                    .findAny()
                    .orElseThrow(() -> new RegionNotFoundException("Региона с такими данными нет"));
        } else {
            throw new RegionNotFoundException("Такого региона не существует");
        }

    }


    public Weather addNewRegion(@NonNull String regionName, @NonNull Weather weather) {

        if (!weatherData.containsKey(regionName)) {

            ArrayList<Weather> arr = new ArrayList<>(1);
            arr.add(weather);
            weatherData.put(regionName, arr);

            return weather;

        } else {
            throw new DuplicateRegionException("Регион с таким именем уже существует");
        }

    }

    public Weather updateTemperatureInRegion(@NonNull String regionName, @NonNull LocalDateTime dateTime, double temperature) {

        if (weatherData.containsKey(regionName)) {

            Weather weather = weatherData.get(regionName).stream()
                    .filter(w -> w.getDateTime().equals(dateTime)).findAny().orElse(null);

            if (weather != null) {
                weather.setTemperature(temperature);
                return weather;
            } else {
                List<Weather> regionWeather = weatherData.computeIfAbsent(regionName, key -> new ArrayList<>());
                regionWeather.add(new Weather(regionName, temperature, dateTime));

                throw new RegionNotFoundException("Региона с данными параметрами нет, но мы его добавили");
            }

        } else {

            ArrayList<Weather> arr = new ArrayList<>(1);
            arr.add(new Weather(regionName, temperature, dateTime));
            weatherData.put(regionName, arr);


            throw new RegionNotFoundException("Региона с данными параметрами нет, но мы его добавили");
        }
    }

    public void deleteRegion(@NonNull String regionName) {

        if (weatherData.containsKey(regionName)) {
            weatherData.remove(regionName);

            throw new DeleteException("Регион успешно удалён");
        } else {
            throw new RegionNotFoundException("Региона с таким именем нет. Мы не можем его удалить.");
        }
    }

}

