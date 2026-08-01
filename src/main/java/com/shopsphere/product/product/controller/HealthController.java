package com.shopsphere.product.product.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${spring.application.version}")
    private String version;

    @GetMapping()
    public ResponseEntity<Map<String,String>> healthCheck() {
        return ResponseEntity.ok(
                Map.of(
                        "service",serviceName,
                        "status", "UP",
                        "version", version
                )
        );
    }
}
