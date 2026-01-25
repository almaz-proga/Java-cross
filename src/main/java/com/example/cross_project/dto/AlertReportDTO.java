package com.example.cross_project.dto;

public record AlertReportDTO(Long id, Long sensorId,  String sensorModel, String type, String timetamp, String description, String status, String photoUrls) {
    public Long getId(){
        return id;
    }
    
    public Long getSensorId(){
        return sensorId;
    }
    public String getSensorModel() {
        return sensorModel;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timetamp;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPhotoUrls() {
        return photoUrls;
    }
    
}
