package com.example.cross_project.mapper;

import java.util.stream.Collectors;

import com.example.cross_project.dto.RoleRequest;
import com.example.cross_project.dto.RoleResponse;
import com.example.cross_project.model.Role;
import com.example.cross_project.model.User;
import com.example.cross_project.model.Permission;

public class RoleMapper {
    public static RoleResponse roleToResonseDTO(Role role) {
        return new RoleResponse(role.getId(),
    role.getTitle(),
    role.getPermissions().stream().map(Permission::getTitle).collect(Collectors.toSet()),
    role.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()));
    }

    public static RoleRequest roleToRequestDTO(Role role) {
        return new RoleRequest(role.getTitle(),
        role.getPermissions().stream().map(Permission::getTitle).collect(Collectors.toSet()),
        role.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()));
    }
}
