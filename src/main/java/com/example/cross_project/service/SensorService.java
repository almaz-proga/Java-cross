package com.example.cross_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cross_project.model.Sensor;
import com.example.cross_project.repository.SensorRepository;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository){
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> getAll(){
        return sensorRepository.findAll();
    }

    public List<Sensor> getAllByModel(String model){
        return sensorRepository.findAllByModel(model);
    }

    public Optional<Sensor> getById(Long id){
        return sensorRepository.findById(id);
    }

    public Sensor create(Sensor sensor){
        return sensorRepository.save(sensor);
    }

    public Optional<Sensor> update(Long id, Sensor sensorDetails){
        return sensorRepository.findById(id).map(sensor -> {
            sensor.setModel(sensorDetails.getModel());
            sensor.setLocation(sensorDetails.getLocation());
            sensor.setAssingnedTo(sensorDetails.getAssingnedTo());
            return sensorRepository.save(sensor);
        });
    }

    public boolean deleteById(Long id){
        if(sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
