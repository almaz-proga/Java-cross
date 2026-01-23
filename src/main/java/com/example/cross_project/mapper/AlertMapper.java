package com.example.cross_project.mapper;

import com.example.cross_project.model.Alert;
import com.example.cross_project.dto.AlertResponse;
import com.example.cross_project.dto.AlertRequest;
public class AlertMapper {
    public static AlertResponse alertToResponseDTO(Alert alert) {
        return new AlertResponse(alert.getId(),
        alert.getSensor().getId(),
        alert.getSensor().getModel(),
        alert.getType(),
        alert.getTimetamp(),
        alert.getDescription(),
        alert.getStatus(),
        alert.getPhotoUrls());
    }
    public static AlertRequest alertToRequestDTO(Alert alert){
        return new AlertRequest(alert.getSensor().getId(),
        alert.getType(),
        alert.getDescription(),
        alert.getPhotoUrls());
    }
}
