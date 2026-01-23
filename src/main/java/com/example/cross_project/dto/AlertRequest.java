package com.example.cross_project.dto;
import com.example.cross_project.enums.EventType;
import java.util.List;
public record AlertRequest(Long sensorId, EventType type, String description, List<String> photoUrls) {
    
}
