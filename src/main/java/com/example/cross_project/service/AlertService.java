package com.example.cross_project.service;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.cross_project.enums.EventType;
import com.example.cross_project.model.Alert;
import com.example.cross_project.model.Sensor;
import com.example.cross_project.dto.AlertRequest;
import com.example.cross_project.dto.AlertResponse;
import com.example.cross_project.enums.StatusType;
import com.example.cross_project.exeptions.ResourceNotFoundException;
import com.example.cross_project.mapper.AlertMapper;
import com.example.cross_project.repository.AlertRepository;
import com.example.cross_project.repository.SensorRepository;
import com.example.cross_project.specifications.AlertSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AlertService {
    
    private final AlertRepository alertRepository;
    private final SensorRepository sensorRepository;


    @Cacheable("alerts")
    public List<AlertResponse> getAll() {
        log.info("Fetching all alerts");

        List<AlertResponse> alerts = alertRepository.findAll().stream()
                .map(AlertMapper::alertToResponseDTO)
                .toList();

        log.info("{} alerts fetched", alerts.size());
        return alerts;
    }

    public List<AlertResponse> getAllByStatus(StatusType statusType) {
            log.info("Fetching alerts by status={}", statusType);

            List<Alert> alerts = alertRepository.findAllByStatus(statusType);

            if (alerts.isEmpty()) {
                log.warn("Alerts with status={} not found", statusType);
                throw new ResolutionException("Alerts with status " + statusType + " not found");
            }

            log.info("{} alerts found with status={}", alerts.size(), statusType);
            return alerts.stream().map(AlertMapper::alertToResponseDTO).toList();
        }

    @Cacheable(value = "alert", key = "#id")
    public AlertResponse getById(Long id) {
        log.debug("Fetching alert by id={}", id);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Alert with id={} not found", id);
                    return new ResourceNotFoundException("Alert with id " + id + " not found");
                });

        log.debug("Alert found: id={}, status={}", alert.getId(), alert.getStatus());
        return AlertMapper.alertToResponseDTO(alert);
    }

    @Transactional
    @CacheEvict(value = "alert", allEntries = true)
    public AlertResponse create(AlertRequest dto) {
        log.info("Creating alert for sensorId={}, type={}", dto.sensorId(), dto.type());

        Sensor sensor = sensorRepository.findById(dto.sensorId())
                .orElseThrow(() -> {
                    log.warn("Sensor id={} not found while creating alert", dto.sensorId());
                    return new ResourceNotFoundException(
                            "Sensor with id " + dto.sensorId() + " not found"
                    );
                });

        Alert alert = new Alert();
        alert.setSensor(sensor);
        alert.setType(dto.type());
        alert.setDescription(dto.description());
        alert.setPhotoUrls(dto.photoUrls());
        alert.setStatus(StatusType.NEW);
        alert.setTimetamp(LocalDateTime.now());

        Alert saved = alertRepository.save(alert);

        log.info("Alert created: id={}, sensorId={}", saved.getId(), sensor.getId());
        return AlertMapper.alertToResponseDTO(saved);
    }

    @Transactional
    @CacheEvict(value = "alert", key ="#id", allEntries = true)
    public AlertResponse update(Long id, AlertRequest dto) {
        log.info("Updating alert id={}", id);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Alert id={} not found for update", id);
                    return new ResourceNotFoundException("Alert with id " + id + " not found");
                });

        if (dto.sensorId() != null) {
            Sensor sensor = sensorRepository.findById(dto.sensorId())
                    .orElseThrow(() -> {
                        log.warn("Sensor id={} not found during alert update", dto.sensorId());
                        return new ResourceNotFoundException(
                                "Sensor with id " + dto.sensorId() + " not found"
                        );
                    });
            alert.setSensor(sensor);
        }

        if (dto.type() != null) alert.setType(dto.type());
        if (dto.description() != null) alert.setDescription(dto.description());
        if (dto.photoUrls() != null) alert.setPhotoUrls(dto.photoUrls());

        alert.setTimetamp(LocalDateTime.now());

        Alert saved = alertRepository.save(alert);

        log.info("Alert id={} updated", id);
        return AlertMapper.alertToResponseDTO(saved);
    }

    @Transactional
    @CacheEvict(value = "alert",key = "#id", allEntries = true)
    public boolean deleteById(Long id) {
        log.info("Attempting to delete alert id={}", id);

        if (alertRepository.existsById(id)) {
            alertRepository.deleteById(id);
            log.info("Alert id={} deleted", id);
            return true;
        }

        log.warn("Cannot delete alert id={} - not found", id);
        return false;
    }

    public Page<AlertResponse> getAllPaged(int page, int size) {
        log.info("Fetching alerts page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<AlertResponse> result =
                alertRepository.findAll(pageable).map(AlertMapper::alertToResponseDTO);

        log.info("Page fetched: {} alerts", result.getNumberOfElements());
        return result;
    }
    
    public Page<AlertResponse> filter(Long sensorId, EventType type, LocalDateTime timetamp,
        String description, StatusType statusType, int page, int size) {
        log.info("Filtering alerts: sensorId={}, type={}, status={}, page={}, size={}",sensorId, type, statusType, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Sensor sensor = null;

        if (sensorId != null) {
            sensor = sensorRepository.findById(sensorId)
                    .orElseThrow(() -> {
                        log.warn("Sensor id={} not found for filtering alerts", sensorId);
                        return new ResourceNotFoundException("Sensor with id " + sensorId + " not found");
                    });
        }

        Specification<Alert> spec = AlertSpecification.filter(sensor, type, timetamp, description, statusType);

        Page<AlertResponse> result = alertRepository.findAll(spec, pageable).map(AlertMapper::alertToResponseDTO);

        log.info("Filtered alerts count={}", result.getNumberOfElements());
        return result;
    }

    @Transactional
    public AlertResponse changeStatus(Long alertId, StatusType newStatus) {
        log.info("Changing status of alert id={} to {}", alertId, newStatus);

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.warn("Alert id={} not found for status change", alertId);
                    return new ResourceNotFoundException("Alert not found");
                });

        alert.setStatus(newStatus);
        Alert saved = alertRepository.save(alert);

        log.info("Alert id={} status changed to {}", alertId, newStatus);
        return AlertMapper.alertToResponseDTO(saved);
    }
    
    @Transactional
    public AlertResponse uploadFiles(Long alertId, List<MultipartFile> files) throws IOException {
        log.info("Uploading {} files to alert id={}", files.size(), alertId);

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> {
                    log.warn("Alert id={} not found for file upload", alertId);
                    return new ResourceNotFoundException("Alert not found");
                });

        if (alert.getPhotoUrls() == null) {
            alert.setPhotoUrls(new ArrayList<>());
        }

        for (MultipartFile file : files) {
            String mimeType = file.getContentType();

            if (!List.of("image/jpeg", "image/png", "image/webp").contains(mimeType)) {
                log.warn("Invalid file type '{}' for alert id={}", mimeType, alertId);
                throw new IllegalArgumentException("Invalid file type: " + mimeType);
            }

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads").resolve(filename);

            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            alert.getPhotoUrls().add("/uploads/" + filename);

            log.debug("File '{}' uploaded for alert id={}", filename, alertId);
        }

        Alert saved = alertRepository.save(alert);

        log.info("Files successfully uploaded to alert id={}", alertId);
        return AlertMapper.alertToResponseDTO(saved);
    }
}
