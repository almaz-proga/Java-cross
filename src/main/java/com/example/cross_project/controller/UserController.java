
package com.example.cross_project.controller;

import com.example.cross_project.model.*;
import com.example.cross_project.dto.UserDTO;
import com.example.cross_project.dto.UserLogged;
import com.example.cross_project.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('USER:READ')")
    public List<UserLogged> getAllUsers(){
            return userService.getUsers();
        }
    
    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('USER:READ')")
    public ResponseEntity<UserLogged> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDto(id));
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO dto){
        UserDTO created = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable Long id, @RequestBody UserLogged dto) {
        return ResponseEntity.ok(userService.update(id,dto));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
