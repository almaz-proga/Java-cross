package com.example.cross_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cross_project.model.*;
import com.example.cross_project.service.RoleService;

import java.util.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.getAll();
    }
    
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getById(@PathVariable Long id){
        return roleService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        Role created = roleService.create(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return roleService.update(id, role)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteById(Long id){
        boolean deleted = roleService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    
}
