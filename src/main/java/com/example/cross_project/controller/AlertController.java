package com.example.cross_project.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.example.cross_project.model.Alert;
import com.example.cross_project.service.AlertService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlertController {
     private final AlertService alertService;

    @GetMapping("/alerts")
    public List<Alert> getAllAlerts(){
        return alertService.getAll();
    }

    @GetMapping("/alerts/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Long id) {
        return alertService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/alerts")
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert) {
        Alert created = alertService.create(alert);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/alerts/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable Long id, @RequestBody Alert alert){
        return alertService.update(id, alert)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/alerts/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id){
        boolean deleted = alertService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
