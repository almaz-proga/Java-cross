package com.example.cross_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cross_project.model.User;

@Repository
public interface UserRepository extends
    JpaRepository<User, Long>{
    List<User> findByUsernameStartingWithIgnoreCase(String username);
    List<User> findAllByUsername(String username);
}
    