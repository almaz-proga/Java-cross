package com.example.cross_project.dto;

import java.util.Set;

import com.example.cross_project.model.Permission;
import com.example.cross_project.model.User;

public record RoleResponse(Long id, String title, Set<String> permissionTitles, Set<String> usernames) {
} 
