package com.example.cross_project.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.cglib.core.Local;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Alert {
    private Long id;
    private Sensor sensor;
    private EventType type;
    private LocalDateTime timetamp;
    private String description;
    private StatusType status;
    private List<String> photoUrls;

    public void setTimestamp(LocalDateTime timestamp) { this.timetamp = timestamp; }
}

