package com.example.cross_project.model;

import java.util.Set;
import org.springframework.cglib.core.Local;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private Set<Role> roles;


}
