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
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User getUser(String username) {
        log.debug("Searching user by username='{}'", username);
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
            log.warn("User with username='{}' not found", username);
            return new ResourceNotFoundException(
                "User with username " + username + " not found"
            );
        });
        log.debug("User found: id={}, username={}", user.getId(), user.getUsername());
                return user;
    }

    public List<UserLogged> getUsers(){
          log.info("Fetching all users");
        List<UserLogged> users = userRepository.findAll().stream()
        .map(UserMapper::userToLoggedDTO).toList();
        log.info("{} users fetched", users.size());
        return users;
    }
    
    public UserLogged getUserDto(Long id) {
        log.debug("Fetching user DTO by id={}", id);
        User user = userRepository.findById(id)
        .orElseThrow(() -> {
            log.warn("User with id={} not found", id);
            return new ResourceNotFoundException("User with id " + id + " not found");
        });
        return UserMapper.userToLoggedDTO(user);
    }

    public UserLogged getUserDto(String username) {
        log.debug("Fetching user DTO by username='{}'", username);
        
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
            log.warn("User with username='{}' not found", username);
            return new ResourceNotFoundException(
                "User with username " + username + " not found"
            );
        });
        return UserMapper.userToLoggedDTO(user);
    }

    public void saveUser(User user){
        log.info("Saving user: username='{}'", user.getUsername());
        userRepository.save(user);
        log.info("User saved successfully");
    }


    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDTO create(UserDTO dto) {
        log.info("Creating user with username='{}'", dto.username());

        Role role = roleRepository.findByTitle(dto.role())
        .orElseThrow(() -> {
            log.warn("Role '{}' not found", dto.role());
            return new ResourceNotFoundException("Role not found");
        });

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEnabled(true);
        user.setRole(role);

         User saved = userRepository.save(user);

        log.info("User created: id={}, username='{}'", saved.getId(), saved.getUsername());
        return UserMapper.userToUserDTO(saved);
    }
    
    @Transactional
    @CachePut(value = "users", key = "#id")
    public UserDTO update(Long id, UserLogged dto) {
        log.info("Updating user id={}", id);

        User user = userRepository.findById(id)
        .orElseThrow(() -> {
            log.warn("User with id={} not found", id);
            return new ResourceNotFoundException("User with id " + id + " not found");
        });

        if (dto.username() != null) {
            log.debug("Updating username to '{}'", dto.username());
            user.setUsername(dto.username());
        }
        if (dto.role() != null) {
            log.debug("Updating role to '{}'", dto.role());
            Role role = roleRepository.findByTitle(dto.role())
            .orElseThrow(() -> {
                log.warn("Role '{}' not found", dto.role());
                return new ResourceNotFoundException("Role not found");
            });
            user.setRole(role);
        }
        log.info("User id={} updated successfully", id);
        return UserMapper.userToUserDTO(user);
        }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public boolean deleteById(Long id) {
        log.info("Attempting to delete user id={}", id);
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User id={} deleted", id);
            return true;
        }
        log.warn("Cannot delete user id={} — not found", id);
        return false;
    }
}

