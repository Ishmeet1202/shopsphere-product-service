package com.shopsphere.product.product.controller;

import com.shopsphere.product.product.dto.response.HealthResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${spring.application.version}")
    private String version;

    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> getHealth() {
        return ResponseEntity.ok(
                HealthResponseDto.builder()
                        .serviceName(serviceName)
                        .status("UP")
                        .version(version)
                        .build()
        );
    }
}
