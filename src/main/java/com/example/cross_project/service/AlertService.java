package com.example.cross_project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cross_project.model.Alert;

import com.example.cross_project.repository.AlertRepository;

@Service
public class AlertService {
    
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository){
        this.alertRepository = alertRepository;
    }

    public List<Alert> getAll(){
        return alertRepository.findAll();
    }

    public List<Alert> getAllBySensor(Long sensorId){
        return alertRepository.findAllBySensor_Id(sensorId);
    }

    public Optional<Alert> getById(Long id){
        return alertRepository.findById(id);
    }

    public Alert create(Alert alert){
        System.out.println("⚙️ Создание alert: " + alert);
        if (alert.getTimetamp() == null) {
            alert.setTimetamp(LocalDateTime.now());
        }
        return alertRepository.save(alert);
    }

    public Optional<Alert> update(Long id, Alert alertDetails){
        return alertRepository.findById(id).map(alert -> {
            alert.setSensor(alertDetails.getSensor());
            alert.setType(alertDetails.getType());
            alert.setTimetamp(LocalDateTime.now());
            alert.setDescription(alertDetails.getDescription());
            alert.setStatus(alertDetails.getStatus());
            alert.setPhotoUrls(alertDetails.getPhotoUrls());
            return alertRepository.save(alert);
        });
    }
    public boolean deleteById(Long id){
        if(alertRepository.existsById(id)) {
            alertRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
