package com.example.cross_project.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.cross_project.mapper.UserMapper;
import com.example.cross_project.dto.UserDTO;
import com.example.cross_project.dto.UserLogged;
import com.example.cross_project.exeptions.ResourceNotFoundException;
import com.example.cross_project.model.User;
import com.example.cross_project.model.Role;

import com.example.cross_project.repository.RoleRepository;
import com.example.cross_project.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
                return user;
    }

    public List<UserLogged> getUsers(){
        return userRepository.findAll().stream().map(UserMapper::userToLoggedDTO).toList(); 
    }
    
    public UserLogged getUserDto(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
                return UserMapper.userToLoggedDTO(user);
    }

    public UserLogged getUserDto(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
                return UserMapper.userToLoggedDTO(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }


    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDTO create(UserDTO dto) {
        Role role = roleRepository.findByTitle(dto.role())
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEnabled(true);
        user.setRole(role);

        return UserMapper.userToUserDTO(userRepository.save(user));
    }
    
    @Transactional
    @CachePut(value = "users", key = "#id")
    public UserDTO update(Long id, UserLogged dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        user.setUsername(dto.username());

        if (dto.username() != null) {
            user.setUsername(dto.username());
        }
        if (dto.role() != null) {
            Role role = roleRepository.findByTitle(dto.role())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            user.setRole(role);
        }
        return UserMapper.userToUserDTO(user);
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

