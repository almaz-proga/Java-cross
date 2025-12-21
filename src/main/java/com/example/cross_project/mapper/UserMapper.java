package com.example.cross_project.mapper;

import com.example.cross_project.dto.UserDTO;
import com.example.cross_project.dto.UserLogged;
import com.example.cross_project.model.User;
import com.example.cross_project.model.Permission;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO userToUserDTO(User user){
        return new UserDTO(user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRole().getAuthority(),
            user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }

    public static UserLogged userToLoggedDTO(User user) {
        return new UserLogged(user.getUsername(),
            user.getRole().getAuthority(),
            user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }
}
