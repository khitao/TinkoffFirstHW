package ru.khodov.springbootapp.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "city")
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

}

