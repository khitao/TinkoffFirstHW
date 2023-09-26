package ru.khodov;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class Weather {

    private final UUID regionId;
    private final String regionName;
    private double temperature;
    private LocalDateTime dateTime;

    private static Map<String, UUID> uniqueIdMap = new HashMap<>();

    public Weather(String regionName, double temperature, LocalDateTime dateTime) {
        this.regionId = generateRegionId(regionName);
        this.regionName = regionName;
        this.temperature = temperature;
        this.dateTime = dateTime;
    }

    private UUID generateRegionId(String regionName) {
        if (uniqueIdMap.containsKey(regionName)) {
            return uniqueIdMap.get(regionName);
        } else {
            UUID regionId = UUID.randomUUID();
            uniqueIdMap.put(regionName, regionId);
            return regionId;
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "regionId=" + regionId +
                ", regionName='" + regionName + '\'' +
                ", temperature=" + temperature +
                ", dateTime=" + dateTime +
                '}';
    }
}

