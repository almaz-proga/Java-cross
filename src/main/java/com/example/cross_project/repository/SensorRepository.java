package com.example.cross_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cross_project.model.Sensor;

@Repository
public interface SensorRepository extends
    JpaRepository<Sensor, Long>{
    List<Sensor> findByModelStartingWithIgnoreCase(String model);
    List<Sensor> findAllByModel(String model);
}
    