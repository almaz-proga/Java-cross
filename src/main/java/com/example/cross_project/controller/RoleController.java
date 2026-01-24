
package com.example.cross_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cross_project.dto.RoleRequest;
import com.example.cross_project.dto.RoleResponse;
import com.example.cross_project.model.*;
import com.example.cross_project.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Roles", description = "Управление ролями системы")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    
    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Получить список ролей", description = "Возвращает список всех ролей в системе")
    public List<RoleResponse> getAllRoles(){
        return roleService.getAll();
    }
    
    @GetMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('USER:READ')")
    @Operation(summary = "Получить роль по ID", description = "Возвращает информацию о роли по её идентификатору")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id){
        return roleService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Создать новую роль", description = "Создает новую роль в системе")
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest role){
        RoleResponse created = roleService.create(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Обновить роль", description = "Обновляет данные существующей роли")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @RequestBody RoleRequest role) {
        return roleService.update(id, role)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Удалить роль", description = "Удаляет роль по идентификатору")
    public ResponseEntity<Void> deleteById(Long id){
        boolean deleted = roleService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    
}