package com.example.cross_project.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;

import com.example.cross_project.repository.AlertRepository;
import com.example.cross_project.dto.AlertResponse;
import com.example.cross_project.mapper.AlertMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AlertRepository alertRepository;

    @Value("${report.template-location}")
    private Resource templateLocation;

    public ByteArrayResource generateAlertReport() {
        List<AlertResponse> alerts = alertRepository.findAll().stream()
            .map(AlertMapper::alertToResponseDTO)
            .toList();

        Map<String, Object> data = Map.of("alerts", alerts);

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
