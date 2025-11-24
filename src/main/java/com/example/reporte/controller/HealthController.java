package com.example.reporte.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(
                Map.of(
                        "service", "reporte-pdf",
                        "status", "ok",
                        "health", "/health",
                        "docs", "/doc/swagger-ui.html",
                        "reportEndpoint", "/api/reporte/pdf"
                )
        );
    }
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
