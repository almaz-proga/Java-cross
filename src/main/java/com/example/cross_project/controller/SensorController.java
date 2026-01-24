package com.example.cross_project.controller;

import com.example.cross_project.dto.SensorRequest;
import com.example.cross_project.dto.SensorResponse;
import com.example.cross_project.model.Sensor;
import com.example.cross_project.service.SensorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




@Tag(name = "Sensors", description = "Управление сенсорами системы")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;

    @GetMapping("/sensors")
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Получить список сенсоров", description = "Возвращает постраничный список сенсоров")
    public Page<SensorResponse> getAllSensors(
        @Parameter(description = "Номер страницы", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Количество элементов на странице", example = "10")
        @RequestParam(defaultValue = "10") int size) {
            return sensorService.getAllPaged(page, size);
        }
    
    @GetMapping("/sensors/{id}")
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Получить сенсор по ID", description = "Возвращает детальную информацию о сенсоре по его идентификатору")
    public ResponseEntity<SensorResponse> getSensorById(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.getById(id));
    }
    
    @PostMapping("/sensors")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Создать новый сенсор", description = "Создает новый сенсор и привязывает его к пользователю")
    public ResponseEntity<SensorResponse> createSensor(@Valid  @RequestBody SensorRequest sensor) {
        SensorResponse created = sensorService.create(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/sensors/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Обновить сенсор", description = "Обновляет модель, локацию или ответственного пользователя сенсора")
    public ResponseEntity<SensorResponse> updateSensor(@Valid @PathVariable Long id, @RequestBody SensorRequest sensor){
        return ResponseEntity.ok(sensorService.update(id, sensor));
    }

    @PutMapping("/sensors/{id}/assign")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Назначить ответственного за сенсор", description = "Назначает пользователя ответственным за конкретный сенсор")
    public ResponseEntity<SensorResponse> assignResponsible(
            @PathVariable("id") Long sensorId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(sensorService.assignResponsible(sensorId, userId));
    }

    @DeleteMapping("/sensors/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Удалить сенсор", description = "Удаляет сенсор по идентификатору")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id){
        boolean deleted = sensorService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    
    
}
    
