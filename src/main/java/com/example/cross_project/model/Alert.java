package com.example.cross_project.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.cglib.core.Local;

public class Alert {
    private Long id;
    private Sensor sensor;
    private EventType type;
    private LocalDateTime timetamp;
    private String description;
    private StatusType status;
    private List<String> photoUrls;

    public Alert(Long id, Sensor sensor, EventType type, LocalDateTime timetamp, String description, StatusType status, List<String> photoUrls) {
        this.id = id;
        this.sensor = sensor;
        this.type = type;
        this.timetamp = timetamp;
        this.description = description;
        this.status = status;
        this.photoUrls = photoUrls;
    }
    public Long getId() { return id; }
    public Sensor getSensor() { return sensor; }
    public EventType getEventType() {return type; }
    public LocalDateTime getLocalDateTime() {return timetamp; }
    public StatusType getStatusType() {return status;}
    public List<String> getPhotoUrls() {return photoUrls; }

    public void setId(Long id) { this.id = id; }
    public void setTimestamp(LocalDateTime timestamp) { this.timetamp = timestamp; }
}

