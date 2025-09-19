
package com.example.cross_project.model;

import java.util.Set;

public class Permission {
    private Long id;
    private String title;
    
    public Permission(Long id, String title){
        this.id = id;
        this.title = title;
    }
    public Long getId() { return id; }
    public String getTitle() {return title; }
}
