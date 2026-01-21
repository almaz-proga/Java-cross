package com.example.cross_project.controller;

import com.example.cross_project.model.*;
import com.example.cross_project.service.PermissonService;

import lombok.RequiredArgsConstructor;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PermissonController {
    private final PermissonService permissonService;

    @GetMapping("/permissions")
    public List<Permission> getAllPermissions() {
        return permissonService.getAll();
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<Permission> getById(@PathVariable Long id){
        return permissonService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermissions(@RequestBody Permission permission){
        Permission created = permissonService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/permissions/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        return permissonService.update(id, permission)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Permission> deleteById(Long id){
        boolean deleted = permissonService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    
}
