package ru.khodov.springbootapp.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.khodov.springbootapp.dto.UpdateRegionTemperatureDto;
import ru.khodov.springbootapp.dto.WeatherDto;
import ru.khodov.springbootapp.dto.WeatherRequestDto;
import ru.khodov.springbootapp.model.Weather;
import ru.khodov.springbootapp.service.WeatherService;


import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;


    @GetMapping("/{regionName}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public double getTemperatureByRegionNameAndDate(@PathVariable String regionName, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return weatherService.getRegionTemperatureForTheCurrentDate(regionName, dateTime);
    }


    @PostMapping("/{regionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WeatherDto> addNewRegion(@PathVariable String regionName, @RequestBody WeatherRequestDto weatherRequestDto) {

        WeatherDto responseDto = weatherService.addNewRegion(regionName, weatherRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    @PutMapping("/{regionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WeatherDto> updateRegionTemperature(@PathVariable String regionName, @RequestBody UpdateRegionTemperatureDto updateRegionTemperatureDto) {

        Weather updateWeather = weatherService.updateTemperatureInRegion(regionName, updateRegionTemperatureDto.getDateTime(), updateRegionTemperatureDto.getTemperature());


        updateWeather.setModificationDate(LocalDateTime.now());

        WeatherDto responseDto = new WeatherDto(updateWeather.getRegionName(), updateWeather.getTemperature());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/{regionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRegion(@PathVariable String regionName) {
        weatherService.deleteRegion(regionName);
    }

}
