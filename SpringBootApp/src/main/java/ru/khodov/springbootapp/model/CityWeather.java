package ru.khodov.springbootapp.model;


import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.time.LocalDateTime;

@Entity
@Table(name = "city_weather")
@Data
@NoArgsConstructor
public class CityWeather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private City city;

    @ManyToOne
    @JoinColumn(name = "weather_type_id", nullable = false)
    private WeatherType weatherType;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private LocalDateTime dateTime;

}

