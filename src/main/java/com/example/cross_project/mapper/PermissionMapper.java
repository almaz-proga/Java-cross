package com.example.cross_project.mapper;

import com.example.cross_project.model.Permission;
import com.example.cross_project.model.Role;

import java.util.stream.Collectors;

import com.example.cross_project.dto.PermissionRequest;
import com.example.cross_project.dto.PermissionResponse;
public class PermissionMapper {
    public static PermissionResponse permissionToResponseDTO(Permission permission){
        return new PermissionResponse(permission.getId(),
        permission.getTitle(),
        permission.getResource(),
        permission.getOperation(),
        permission.getRoles().stream().map(Role::getTitle).collect(Collectors.toSet()));
    }

    public static PermissionRequest permissionToRequestDTO(Permission permission){
        return new PermissionRequest(permission.getTitle(),
        permission.getResource(),
        permission.getOperation(),
        permission.getRoles().stream().map(Role::getTitle).collect(Collectors.toSet()));

    }
}
