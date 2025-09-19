package com.example.cross_project.controller;

import com.example.cross_project.model.*;

import java.util.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UserController {
    public static List<User> users = new ArrayList<>(Arrays.asList(
            new User(1L, "Anton", "1234", true, Set.of(new Role(1L, "ADMIN", Set.of(new Permission(1L, "READ"), new Permission(2L, "WRITE"))))
            ),
            new User(2L, "Egor", "4321", true, Set.of(new Role(2L, "USER", Set.of(new Permission(1L, "READ"))))
            )
    ));
    @GetMapping("/users")
    public List<User> getUsers(){
        return users;
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        for(User user : users){
            if(user.getId().equals(id)){
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user){
        user.setId((long) users.size() + 1);
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
}
