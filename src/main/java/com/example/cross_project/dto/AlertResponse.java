package com.example.cross_project.dto;
import com.example.cross_project.enums.EventType;
import com.example.cross_project.enums.StatusType;

import java.time.LocalDateTime;
import java.util.List;
public record AlertResponse(Long id, Long sensorId, String sensorModel, EventType type, LocalDateTime timetamp, String description, StatusType status, List<String> photoUrls) {
    
}
