package com.example.cross_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cross_project.model.Alert;

@Repository
public interface AlertRepository extends
    JpaRepository<Alert, Long>{
    List<Alert> findAllBySensor_Id(Long sensorId);
}
