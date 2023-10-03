package ru.khodov.springbootapp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Weather {

    private final UUID regionId;
    private final String regionName;
    private double temperature;
    private LocalDateTime dateTime;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

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


}