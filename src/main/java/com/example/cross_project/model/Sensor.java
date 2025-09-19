package com.example.cross_project.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.swing.event.DocumentEvent.EventType;

public class Sensor {
    
    private Long id; 
    private String model; 
    private String location;
    private User assingnedTo;

    public Sensor(Long id, String model, String location, User assignedTo){
        this.id = id;
        this.model = model;
        this.location = location;
        this.assingnedTo = assignedTo;
    }
    
    public Long getId() {return id; }
    public String getModel() { return model; }
    public String getLocation() {return location; }
    public User getAssingnedTo() {return assingnedTo; }

    public void setId(Long id) {this.id = id; }

    
}
