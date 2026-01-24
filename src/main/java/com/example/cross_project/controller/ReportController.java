package com.example.cross_project.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.core.io.Resource;
import org.jxls.common.Context;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.cross_project.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "Reports", description = "Создание xml отчётов об инцидентах")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/alerts")
    @PreAuthorize("hasAuthority('ADMIN:WRITE')")
    @Operation(summary = "Скачать отчет инцидентов", description = "Формирует Excel отчет по всем инцидентам")
    public ResponseEntity<Resource> downloadAlertReport() {
        Resource resource = reportService.generateAlertReport();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=alerts.xlsx")
                .body(resource);
    }
}
