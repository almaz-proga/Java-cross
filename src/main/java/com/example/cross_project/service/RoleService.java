
package com.example.cross_project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cross_project.repository.PermissionRepository;
import com.example.cross_project.repository.RoleRepository;
import com.example.cross_project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.example.cross_project.dto.RoleResponse;
import com.example.cross_project.dto.RoleRequest;
import com.example.cross_project.mapper.RoleMapper;
import com.example.cross_project.model.Role;

@RequiredArgsConstructor
@Service
public class RoleService {
   private final RoleRepository roleRepository;
   private final PermissionRepository permissionRepository;
   private final UserRepository userRepository; 

   public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(RoleMapper::roleToResonseDTO).toList();
   }
   public Optional<RoleResponse> getById(Long id) {
        return roleRepository.findById(id).map(RoleMapper::roleToResonseDTO);
   }
   
   public RoleResponse create(RoleRequest request) {
        Role role = new Role();
        role.setTitle(request.title());
        role.setPermissions(permissionRepository.findByTitleIn(request.permissiontitles()));
        role.setUsers(userRepository.findByUsernameIn(request.usernames())
);

        Role saved = roleRepository.save(role);
        return RoleMapper.roleToResonseDTO(saved);
    }

   public Optional<RoleResponse> update(Long id, RoleRequest request) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setTitle(request.title());
                    role.setPermissions(permissionRepository.findByTitleIn(request.permissiontitles()));
                    role.setUsers(userRepository.findByUsernameIn(request.usernames()));
                    return roleRepository.save(role);
                })
                .map(RoleMapper::roleToResonseDTO);
    }
   public boolean deleteById(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
   }
}
