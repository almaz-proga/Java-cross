package com.example.cross_project.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.swing.event.DocumentEvent.EventType;
import org.springframework.cglib.core.Local;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sensor {
    
    private Long id; 
    private String model; 
    private String location;
    private User assingnedTo;
}
