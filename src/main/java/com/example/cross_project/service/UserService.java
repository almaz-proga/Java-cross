package com.example.cross_project.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.cross_project.mapper.UserMapper;
import com.example.cross_project.dto.UserDTO;
import com.example.cross_project.exeptions.ResourceNotFoundException;
import com.example.cross_project.model.User;
import com.example.cross_project.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
                return user;
    }

    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(UserMapper::userToUserDTO).toList(); 
    }
    
    public UserDTO getUserDto(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
                return UserMapper.userToUserDTO(user);
    }

    public UserDTO getUserDto(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
                return UserMapper.userToUserDTO(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public Page<User> getAllPaged(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Cacheable(value = "users", key ="#id")
    public Optional<User> getById(Long id){
        return userRepository.findById(id);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        return userRepository.save(user);
    }
    @Transactional
    @CachePut(value = "users", key = "#id")
    public Optional<User> update(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEnabled(userDetails.isEnabled());
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        });
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public boolean deleteById(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

