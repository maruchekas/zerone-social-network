package com.skillbox.javapro21.repository;

import com.skillbox.javapro21.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {

    Optional<City> findCityByName(String city);

    Optional<City> findCityById(Integer id);
}