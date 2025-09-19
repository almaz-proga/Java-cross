package com.example.cross_project.controller;

import com.example.cross_project.model.Sensor;
import com.example.cross_project.controller.UserController;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class SensorController {
    public static List<Sensor> sensors = new ArrayList<>(Arrays.asList(
        new Sensor(1L, "model-1", "room-101", UserController.users.get(0)),
        new Sensor(2L, "model-2", "room-102", UserController.users.get(1))
    ));
    @GetMapping("/sensors")
    public List<Sensor> getSensors(){
        return sensors;
    }
    @GetMapping("/sensors/{id}")
    public ResponseEntity<Sensor> getSensor(@PathVariable Long id){
        for(Sensor sensor : sensors){
            if(sensor.getId().equals(id)){
                return ResponseEntity.ok(sensor);
            }
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/sensors")
    public ResponseEntity<Sensor> addSensor(@RequestBody @Valid Sensor sensor) {
        sensor.setId((long) sensors.size() + 1);
        sensors.add(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(sensor);
    }
}
    
