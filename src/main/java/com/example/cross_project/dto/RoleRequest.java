package com.example.cross_project.dto;

import java.util.Set;

import com.example.cross_project.model.Permission;
import com.example.cross_project.model.User;

public record RoleRequest(String title, Set<String> permissiontitles, Set<String> usernames) {
    
}
