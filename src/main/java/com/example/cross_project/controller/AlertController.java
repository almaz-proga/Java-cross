package com.example.cross_project.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.example.cross_project.model.Alert;
import com.example.cross_project.model.EventType;
import com.example.cross_project.model.StatusType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class AlertController {
     private List<Alert> alerts = new ArrayList<>(Arrays.asList(
        new Alert(1L,
                SensorController.sensors.get(0),
                EventType.ACCIDENT,
                LocalDateTime.now(),
                "Car accident detected",
                StatusType.NEW,
                List.of("photo1.jpg", "photo2.jpg")),
        new Alert(2L,
                SensorController.sensors.get(1), 
                EventType.BUTTON,
                LocalDateTime.now(),
                "Emergency button pressed",
                StatusType.IN_PROGRESS,
                List.of("button.jpg"))
    ));

    @GetMapping("/alerts")
    public List<Alert> getAlerts() {
        return alerts;
    }

    @GetMapping("/alerts/{id}")
    public ResponseEntity<Alert> getAlert(@PathVariable Long id) {
        return alerts.stream()
                .filter(alert -> alert.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alerts")
    public ResponseEntity<Alert> addAlert(@RequestBody Alert alert) {
        alert.setId((long) alerts.size() + 1);
        alert.setTimestamp(LocalDateTime.now()); 
        alerts.add(alert);
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }
    
}
