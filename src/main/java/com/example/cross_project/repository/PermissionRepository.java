package com.example.cross_project.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cross_project.model.Permission;
import com.example.cross_project.model.Role;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByResourceAndOperation(String resource, String operation);
    Set<Permission> findByTitleIn(Set<String> titles);
;
}
