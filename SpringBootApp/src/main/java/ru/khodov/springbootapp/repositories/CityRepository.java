package ru.khodov.springbootapp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khodov.springbootapp.model.City;


@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
