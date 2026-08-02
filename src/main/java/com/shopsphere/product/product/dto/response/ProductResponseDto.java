package com.shopsphere.product.product.dto.response;

import com.shopsphere.product.product.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {
    private String id;
    private String name;
    private String sku;
    private Status status;
}
