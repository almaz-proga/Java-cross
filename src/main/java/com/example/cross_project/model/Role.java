package com.example.cross_project.model;

import java.util.Set;
public class Role {
    private Long id;
    private String title;
    private Set<Permission> permissions;

    public Role(Long id, String title, Set<Permission> permissions){
        this.id = id;
        this.title = title;
        this.permissions = permissions;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Set<Permission> getPermission() {return permissions;}

}
