package com.example.cross_project.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cross_project.model.User;
import com.example.cross_project.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> getAllByUsername(String username){
        return userRepository.findAllByUsername(username);
    }
    public User create(User user) {
        return userRepository.save(user);
    }
    public User getById(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public User update(Long id, User user){
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        }).orElse(null);
    }

    public boolean deleteById(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

