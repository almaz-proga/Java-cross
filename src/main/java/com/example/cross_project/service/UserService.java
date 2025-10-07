package com.example.cross_project.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<User> getById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> update(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEnabled(userDetails.isEnabled());
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        });
    }

    public boolean deleteById(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

