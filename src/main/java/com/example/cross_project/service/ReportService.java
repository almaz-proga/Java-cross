package com.example.cross_project.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;

import com.example.cross_project.repository.AlertRepository;
import com.example.cross_project.dto.AlertReportDTO;
import com.example.cross_project.dto.AlertResponse;
import com.example.cross_project.mapper.AlertMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AlertService alertService;

    @Value("${report.template-location}")
    private Resource templateLocation;

    public ByteArrayResource generateAlertReport() {
        
        List<AlertResponse> alerts = alertService.getAll();

        List<AlertReportDTO> alertReports = alerts.stream()
            .map(alert -> new AlertReportDTO(
                alert.id(),
                alert.sensorId(),
                alert.sensorModel(),
                alert.type().name(),
                alert.timetamp().toString(),
                alert.description(),
                alert.status().name(),
                String.join(", ", alert.photoUrls())
            ))
            .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("alerts", alertReports);

        try (InputStream is = templateLocation.getInputStream()) {
            byte[] reportBytes = JxlsPoiTemplateFillerBuilder.newInstance()
                    .withTemplate(is)
                    .buildAndFill(data);
            return new ByteArrayResource(reportBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating the report", e);
        }
    }
}
