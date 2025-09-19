package com.example.cross_project.model;

import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private Set<Role> roles;
    
    public User(Long id, String username, String password, boolean enabled,Set<Role> roles){
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }
    public Long getId() { return id; }
    public String getUserName() { return username; }
    public String getPassword() {return password; }
    public boolean isEnabled() {return enabled; }
    public Set<Role> getRoles() {return roles;}


    public void setId(Long id) { this.id = id; }
    
   
}
