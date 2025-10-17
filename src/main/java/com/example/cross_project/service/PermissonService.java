package com.example.cross_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cross_project.repository.PermissionRepository;
import com.example.cross_project.model.Permission;
import com.example.cross_project.model.Role;
import com.example.cross_project.model.User;

@Service
public class PermissonService {
    private final PermissionRepository permissionRepository;

    public PermissonService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    public List<Permission> getAll() {
        return permissionRepository.findAll();
   }
   public Optional<Permission> getById(Long id) {
        return permissionRepository.findById(id);
   }
   public Permission create(Permission permission){
        return permissionRepository.save(permission);
   }
   public Optional<Permission> update(Long id, Permission permissionDetails) {
        return permissionRepository.findById(id).map(permission -> {
            permission.setTitle(permissionDetails.getTitle());
            return permissionRepository.save(permission);
        });
    }

   public boolean deleteById(Long id) {
        if (permissionRepository.existsById(id)) {
            permissionRepository.deleteById(id);
            return true;
        }
        return false;
   }

}
