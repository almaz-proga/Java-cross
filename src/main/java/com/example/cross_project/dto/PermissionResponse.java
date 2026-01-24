package com.example.cross_project.dto;

import java.util.Set;

import com.example.cross_project.model.Role;

public record PermissionResponse(Long id, String title, String resource, String operation, Set<String> roleTitles) {
    
}
