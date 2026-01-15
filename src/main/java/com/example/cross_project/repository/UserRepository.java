package com.example.cross_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cross_project.model.User;

@Repository
public interface UserRepository extends
    JpaRepository<User, Long>{
        Optional <User> findByUsername (String username);
}
    