package ru.khodov;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws ParseException {

        List<Weather> weatherList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        weatherList.add(new Weather("Москва", -21, dateFormat.parse("2023-12-21 10:00:00")));
        weatherList.add(new Weather("Воронеж", -20, dateFormat.parse("2023-12-21 10:00:00")));
        weatherList.add(new Weather("Липецк", 22, dateFormat.parse("2023-09-01 19:00:00")));
        weatherList.add(new Weather("Москва", 22, dateFormat.parse("2023-09-21 19:00:00")));
        weatherList.add(new Weather("Елец", 5, dateFormat.parse("2023-11-21 13:00:00")));
        weatherList.add(new Weather("Волгоград", 30, dateFormat.parse("2023-06-21 15:30:00")));


        System.out.println("Среднее значение температуры в регионах: " + averageTemperature(weatherList));


        System.out.println(temperaturesAreHigherThan(weatherList, -22));


        for (Map.Entry<UUID, List<Integer>> entry : mapIdToTemperature(weatherList).entrySet()) {
            UUID regionId = entry.getKey();
            List<Integer> temperatures = entry.getValue();

            System.out.println("RegionId " + regionId + ": " + temperatures);
        }


        for (Map.Entry<Integer, List<Weather>> entry : mapTemperatureToObjects(weatherList).entrySet()) {
            int temperature = entry.getKey();
            List<Weather> object = entry.getValue();

            System.out.println("Температура " + temperature + ": " + object);
        }
    }

    private static double averageTemperature(List<Weather> weatherList) {
        return weatherList.stream().mapToDouble(Weather::getTemperature).average().orElse(0.0);
    }

    private static List<String> temperaturesAreHigherThan(List<Weather> weatherList, int desiredTemperature) {
        return weatherList.stream().filter(weather -> weather.getTemperature() > desiredTemperature).map(Weather::getRegionName).distinct().toList();
    }


    private static Map<UUID, List<Integer>> mapIdToTemperature(List<Weather> weatherList) {
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

    private static Map<Integer, List<Weather>> mapTemperatureToObjects(List<Weather> weatherList) {
        return weatherList.stream()
                .collect(
                        Collectors.groupingBy(
                                Weather::getTemperature
                        )
                );
    }
}