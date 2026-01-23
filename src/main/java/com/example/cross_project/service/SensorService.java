package com.example.cross_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.cross_project.dto.SensorRequest;
import com.example.cross_project.dto.SensorResponse;
import com.example.cross_project.exeptions.ResourceNotFoundException;
import com.example.cross_project.mapper.SensorMapper;
import com.example.cross_project.model.Sensor;
import com.example.cross_project.model.User;
import com.example.cross_project.repository.SensorRepository;
import com.example.cross_project.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;

    @Cacheable("sensors")
    public List<SensorResponse> getAll(){
        return sensorRepository.findAll().stream().map(SensorMapper::sensorToResponseDTO).toList();
    }

    public List<SensorResponse> getAllByModel(String model){
        List<Sensor> sensors = sensorRepository.findAllByModel(model);
        if (sensors.isEmpty()) {
            throw new ResourceNotFoundException("Sensors with model " + model + " not found");
        }
            return sensors.stream().map(SensorMapper::sensorToResponseDTO).toList();
    }

    @Cacheable(value = "sensor", key = "#id")
    public SensorResponse getById(Long id){
        Sensor sensor = sensorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sensor with id " + id + "not found"));
        return SensorMapper.sensorToResponseDTO(sensor);
        
    }

    @Transactional
    @CacheEvict(value = "sensor", allEntries = true)
    public SensorResponse create(SensorRequest dto){
        User user = userRepository.findById(dto.assignedUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User with id" + dto.assignedUserId() + "not found"));
        
        Sensor sensor = new Sensor();
        sensor.setModel(dto.model());
        sensor.setLocation(dto.location());
        sensor.setAssingnedTo(user);

        return SensorMapper.sensorToResponseDTO(sensorRepository.save(sensor));

    }

    @Transactional
    @CacheEvict(value = "sensor", key ="#id", allEntries = true)
    public SensorResponse update(Long id, SensorRequest dto){
        Sensor sensor = sensorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sensor with id" + id + "not found"));

        if (dto.model() != null) {
            sensor.setModel(dto.model());
        }

        if (dto.location() != null) {
            sensor.setLocation(dto.location());
        }

        if (dto.assignedUserId() != null) {
            User user = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            sensor.setAssingnedTo(user);
        }

    return SensorMapper.sensorToResponseDTO(sensor);
    }

    @Transactional
    @CacheEvict(value = "sensor", key="#id", allEntries = true)
    public boolean deleteById(Long id){
        if(sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<SensorResponse> getAllPaged(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return sensorRepository.findAll(pageable).map(SensorMapper::sensorToResponseDTO);
    }
    
    @Transactional
    public SensorResponse assignResponsible(Long sensorId, Long userId) {
    Sensor sensor = sensorRepository.findById(sensorId)
            .orElseThrow(() -> new ResourceNotFoundException("Sensor not found"));

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    sensor.setAssingnedTo(user);

    return SensorMapper.sensorToResponseDTO(sensorRepository.save(sensor));
}
}
