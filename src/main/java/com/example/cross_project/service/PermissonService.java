
package com.example.cross_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cross_project.repository.PermissionRepository;
import com.example.cross_project.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

import com.example.cross_project.dto.PermissionRequest;
import com.example.cross_project.dto.PermissionResponse;
import com.example.cross_project.mapper.PermissionMapper;
import com.example.cross_project.model.Permission;
import com.example.cross_project.model.Role;
import com.example.cross_project.model.User;

@Service
@RequiredArgsConstructor
public class PermissonService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream().map(PermissionMapper::permissionToResponseDTO).toList();
   }
   public Optional<PermissionResponse> getById(Long id) {
        return permissionRepository.findById(id).map(PermissionMapper::permissionToResponseDTO);
   }

   public PermissionResponse create(PermissionRequest request) {
        Permission permission = new Permission();
        permission.setTitle(request.title());
        permission.setResource(request.resource());
        permission.setOperation(request.operation());
        permission.setRoles(roleRepository.findByTitleIn(request.roleTitles()));

        Permission saved = permissionRepository.save(permission);
        return PermissionMapper.permissionToResponseDTO(saved);
    }

    public Optional<PermissionResponse> update(Long id, PermissionRequest request) {
        return permissionRepository.findById(id)
                .map(permission -> {
                    permission.setTitle(request.title());
                    permission.setResource(request.resource());
                    permission.setOperation(request.operation());
                    permission.setRoles(roleRepository.findByTitleIn(request.roleTitles()));
                    return permissionRepository.save(permission);
                })
                .map(PermissionMapper::permissionToResponseDTO);
    }


   public boolean deleteById(Long id) {
        if (permissionRepository.existsById(id)) {
            permissionRepository.deleteById(id);
            return true;
        }
        return false;
   }

}

