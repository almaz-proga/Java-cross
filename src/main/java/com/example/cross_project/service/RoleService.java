
package com.example.cross_project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cross_project.repository.RoleRepository;
import com.example.cross_project.model.Role;


@Service
public class RoleService {
   private final RoleRepository roleRepository;

   public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
   }
   public List<Role> getAll() {
        return roleRepository.findAll();
   }
   public Optional<Role> getById(Long id) {
        return roleRepository.findById(id);
   }
   public Role create(Role role){
        return roleRepository.save(role);
   }
   public Optional<Role> update(Long id, Role roleDetails) {
        return roleRepository.findById(id).map(role -> {
            role.setTitle(roleDetails.getTitle());
            return roleRepository.save(role);
        });
    }
   public boolean deleteById(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
   }
}
