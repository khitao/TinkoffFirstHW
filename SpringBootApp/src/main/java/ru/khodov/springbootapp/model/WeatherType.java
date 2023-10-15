package ru.khodov.springbootapp.model;


import jakarta.persistence.*;
import lombok.Data;




@Entity
@Table(name = "weather_type")
@Data
public class WeatherType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

}
