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
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SensorService {

    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;

    @Cacheable("sensors")
    public List<SensorResponse> getAll(){
        log.info("Fetching all sensors");

        List<SensorResponse> sensors = sensorRepository.findAll().stream()
            .map(SensorMapper::sensorToResponseDTO).toList();

        log.info("{} sensors fetched", sensors.size());
        return sensors;
    }

    public List<SensorResponse> getAllByModel(String model){
        log.info("Fetching sensors by model='{}'", model);

        List<Sensor> sensors = sensorRepository.findAllByModel(model);

        if (sensors.isEmpty()) {
            log.warn("Sensors with model='{}' not found", model);
            throw new ResourceNotFoundException(
                "Sensors with model " + model + " not found"
            );
        }

        log.info("{} sensors found for model='{}'", sensors.size(), model);
        return sensors.stream().map(SensorMapper::sensorToResponseDTO).toList();
    }

    @Cacheable(value = "sensor", key = "#id")
    public SensorResponse getById(Long id){
        log.debug("Fetching sensor by id={}", id);
         Sensor sensor = sensorRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Sensor with id={} not found", id);
                return new ResourceNotFoundException(
                    "Sensor with id " + id + " not found"
                );
            });

        log.debug("Sensor found: id={}, model={}", sensor.getId(), sensor.getModel());
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
        log.info("Attempting to delete sensor id={}", id);

        if (sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            log.info("Sensor id={} deleted", id);
            return true;
        }

        log.warn("Cannot delete sensor id={} - not found", id);
        return false;
    }

    public Page<SensorResponse> getAllPaged(int page, int size){
       log.info("Fetching sensors page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<SensorResponse> result =
                sensorRepository.findAll(pageable).map(SensorMapper::sensorToResponseDTO);

        log.info("Page fetched: {} sensors", result.getNumberOfElements());
        return result;
    }
    
    @Transactional
    public SensorResponse assignResponsible(Long sensorId, Long userId) {
    log.info("Assigning user id={} to sensor id={}", userId, sensorId);

    Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> {
                log.warn("Sensor id={} not found", sensorId);
                return new ResourceNotFoundException("Sensor not found");
            });

    User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User id={} not found", userId);
                return new ResourceNotFoundException("User not found");
            });

    sensor.setAssingnedTo(user);
    Sensor saved = sensorRepository.save(sensor);

    log.info("User id={} assigned to sensor id={}", userId, sensorId);
    return SensorMapper.sensorToResponseDTO(saved);
}
}
