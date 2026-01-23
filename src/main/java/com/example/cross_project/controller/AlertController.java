package com.example.cross_project.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cross_project.dto.AlertRequest;
import com.example.cross_project.dto.AlertResponse;
import com.example.cross_project.enums.StatusType;
import com.example.cross_project.enums.EventType;
import com.example.cross_project.model.Alert;
import com.example.cross_project.service.AlertService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlertController {
     private final AlertService alertService;

    @GetMapping("/alerts")
    @PreAuthorize("hasAuthority('USER:READ')")
    public Page<AlertResponse> filterAlerts(
        @RequestParam(required = false) Long sensorId,
        @RequestParam(required = false) EventType type,
        @RequestParam(required = false) LocalDateTime timetamp,
        @RequestParam(required = false) StatusType statusType,
        @RequestParam(required = false) String description,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
           return alertService.filter(sensorId, type, timetamp, description, statusType, page, size);
    }
    
    @GetMapping("/alerts/{id}")
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.getById(id));
    }
    
    @PostMapping("/alerts")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    public ResponseEntity<AlertResponse> createAlert(@Valid @RequestBody AlertRequest alert) {
        AlertResponse created = alertService.create(alert);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping(value = "/alerts/{id}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Добавить фото к алерту", description = "Можно загрузить несколько изображений")
    public ResponseEntity<AlertResponse> uploadAlerPhotos(
        @PathVariable Long id, @RequestPart("file") MultipartFile[] files
    ) throws IOException {
        return ResponseEntity.ok(alertService.uploadFiles(id, Arrays.asList(files)));
    }

    @PutMapping("/alerts/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    public ResponseEntity<AlertResponse> updateAlert(@PathVariable Long id, @RequestBody AlertRequest alert){
        return ResponseEntity.ok(alertService.update(id, alert));
    }

    @PutMapping("/alerts/{id}/status_change")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    public ResponseEntity<AlertResponse> changeStatus(@PathVariable Long id, @RequestParam StatusType status) {
        return ResponseEntity.ok(alertService.changeStatus(id, status));
    }
    
    @DeleteMapping("/alerts/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id){
        boolean deleted = alertService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
