package com.example.cross_project.mapper;
import com.example.cross_project.model.Sensor;
import com.example.cross_project.dto.SensorRequest;
import com.example.cross_project.dto.SensorResponse;

public class SensorMapper {
    public static SensorResponse sensorToResponseDTO(Sensor sensor) {
        return new SensorResponse(sensor.getId(),
            sensor.getModel(),
            sensor.getLocation(),
            sensor.getAssingnedTo().getUsername());
    }
    public static SensorRequest sensorToRequestDTO(Sensor sensor) {
        return new SensorRequest(sensor.getModel(),
            sensor.getLocation(),
            sensor.getAssingnedTo().getId());
    }
}
