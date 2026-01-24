package com.example.cross_project.controller;

import com.example.cross_project.dto.PermissionRequest;
import com.example.cross_project.dto.PermissionResponse;
import com.example.cross_project.model.*;
import com.example.cross_project.service.PermissonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Permissions", description = "Управление разрешение системы")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PermissonController {
    private final PermissonService permissonService;

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Получить список разрешений", description = "Возвращает список всех разрешений")
    public List<PermissionResponse> getAllPermissions() {
        return permissonService.getAll();
    }

    @GetMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Получить разрешение по ID", description = "Возвращает информацию о разрешении по его идентификатору")
    public ResponseEntity<PermissionResponse> getById(@PathVariable Long id){
        return permissonService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Создать новое разрешение", description = "Создает новое разрешение")
    public ResponseEntity<PermissionResponse> createPermissions(@RequestBody PermissionRequest permission){
        PermissionResponse created = permissonService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Обновить разрешение", description = "Обновляет данные разрешения)")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id, @RequestBody PermissionRequest permission) {
        return permissonService.update(id, permission)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/permissions/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Удалить разрешение", description = "Удаляет разрешение по идентификатору")
    public ResponseEntity<Permission> deleteById(Long id){
        boolean deleted = permissonService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    
}
