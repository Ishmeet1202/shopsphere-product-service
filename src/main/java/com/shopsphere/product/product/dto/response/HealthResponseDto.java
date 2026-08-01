package com.shopsphere.product.product.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HealthResponseDto {
    private String serviceName;
    private String status;
    private String version;
}
