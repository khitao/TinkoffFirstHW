package ru.khodov.springbootapp.service;


import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.khodov.springbootapp.dto.WeatherDto;
import ru.khodov.springbootapp.dto.WeatherRequestDto;
import ru.khodov.springbootapp.model.Weather;
import ru.khodov.springbootapp.util.DeleteException;
import ru.khodov.springbootapp.util.DuplicateException;
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


    public WeatherDto addNewRegion(@NonNull String regionName, @NonNull WeatherRequestDto weatherRequestDto) {

        if (!weatherData.containsKey(regionName)) {

            Weather weather = new Weather(regionName, weatherRequestDto.getTemperature(), weatherRequestDto.getDateTime());

            LocalDateTime localDateTimeNow = LocalDateTime.now();
            weather.setCreationDate(localDateTimeNow);
            weather.setModificationDate(localDateTimeNow);

            ArrayList<Weather> arr = new ArrayList<>(1);
            arr.add(weather);
            weatherData.put(regionName, arr);

            return new WeatherDto(weather.getRegionName(), weather.getTemperature());

        } else {
            throw new DuplicateException("Регион с таким именем уже существует");
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

                Weather newWeather = new Weather(regionName, temperature, dateTime);

                LocalDateTime localDateTimeNow = LocalDateTime.now();
                newWeather.setCreationDate(localDateTimeNow);
                newWeather.setModificationDate(localDateTimeNow);

                regionWeather.add(newWeather);

                throw new RegionNotFoundException("Региона с данными параметрами нет, но мы его добавили");
            }

        } else {

            ArrayList<Weather> arr = new ArrayList<>(1);

            Weather newWeather = new Weather(regionName, temperature, dateTime);

            LocalDateTime localDateTimeNow = LocalDateTime.now();
            newWeather.setCreationDate(localDateTimeNow);
            newWeather.setModificationDate(localDateTimeNow);

            arr.add(newWeather);
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

