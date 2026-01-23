package com.example.cross_project.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.cross_project.enums.StatusType;
import com.example.cross_project.model.Alert;

@Repository
public interface AlertRepository extends
    JpaRepository<Alert, Long>,
    JpaSpecificationExecutor<Alert>{
    List<Alert> findAllBySensor_Id(Long sensorId);
    List<Alert> findAllByStatus(StatusType status);

}
