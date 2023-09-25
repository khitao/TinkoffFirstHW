package ru.khodov;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<Weather> weatherList = new ArrayList<>();


        weatherList.add(new Weather("Москва", 21.0, LocalDateTime.of(2023, 12, 21, 10, 0, 0)));
        weatherList.add(new Weather("Воронеж", -20.0, LocalDateTime.of(2023, 12, 21, 10, 0, 0)));
        weatherList.add(new Weather("Липецк", 22.0, LocalDateTime.of(2023, 9, 1, 19, 0, 0)));
        weatherList.add(new Weather("Москва", 22.0, LocalDateTime.of(2023, 9, 21, 19, 0, 0)));
        weatherList.add(new Weather("Елец", 5.0, LocalDateTime.of(2023, 11, 21, 13, 0, 0)));
        weatherList.add(new Weather("Волгоград", 30.0, LocalDateTime.of(2023, 6, 21, 15, 30, 0)));


        for (Map.Entry<String, Double> entry : averageTemperature(weatherList).entrySet()) {
            String region = entry.getKey();
            double avrTemperature = entry.getValue();

            System.out.println(region + " средняя температура: " + avrTemperature);
        }


        System.out.println(temperaturesAreHigherThan(weatherList, -22.0));


        for (Map.Entry<UUID, List<Double>> entry : mapIdToTemperature(weatherList).entrySet()) {
            UUID regionId = entry.getKey();
            List<Double> temperatures = entry.getValue();

            System.out.println("RegionId " + regionId + ": " + temperatures);
        }


        for (Map.Entry<Double, List<Weather>> entry : mapTemperatureToObjects(weatherList).entrySet()) {
            double temperature = entry.getKey();
            List<Weather> object = entry.getValue();

            System.out.println("Температура " + temperature + ": " + object);
        }
    }

    private static Map<String, Double> averageTemperature(List<Weather> weatherList) {
        return weatherList.stream().
                collect(
                        Collectors.groupingBy(
                                Weather::getRegionName,
                                Collectors.averagingDouble(
                                        Weather::getTemperature
                                )
                        )

                );
    }


    private static List<String> temperaturesAreHigherThan(List<Weather> weatherList, double desiredTemperature) {
        return weatherList.stream().filter(weather -> weather.getTemperature() > desiredTemperature).map(Weather::getRegionName).distinct().toList();
    }


    private static Map<UUID, List<Double>> mapIdToTemperature(List<Weather> weatherList) {
        return weatherList.stream()
                .collect(
                        Collectors.groupingBy(
                                Weather::getRegionId,
                                Collectors.mapping(
                                        Weather::getTemperature,
                                        Collectors.toList()
                                )
                        )
                );
    }

    private static Map<Double, List<Weather>> mapTemperatureToObjects(List<Weather> weatherList) {
        return weatherList.stream()
                .collect(
                        Collectors.groupingBy(
                                Weather::getTemperature
                        )
                );
    }
}
