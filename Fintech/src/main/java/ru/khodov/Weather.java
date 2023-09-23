package ru.khodov;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Weather {

    private final UUID regionId;
    private String regionName;
    private int temperature;
    private Date dateTime;

    private static Map<String, UUID> regionIdMap = new HashMap<>();

    public Weather(String regionName, int temperature, Date dateTime) {
        this.regionId = generateRegionId(regionName);
        this.regionName = regionName;
        this.temperature = temperature;
        this.dateTime = dateTime;
    }

    private UUID generateRegionId(String regionName) {
        if (regionIdMap.containsKey(regionName)) {
            return regionIdMap.get(regionName);
        } else {
            UUID regionId = UUID.randomUUID();
            regionIdMap.put(regionName, regionId);
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
