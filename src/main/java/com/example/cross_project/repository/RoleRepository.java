package com.example.cross_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cross_project.model.Role;

@Repository
public interface RoleRepository extends
    JpaRepository<Role, Long> {
    Optional<Role> findByTitle(String title);
}
