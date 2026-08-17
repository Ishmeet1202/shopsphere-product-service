package com.shopsphere.product.product.dto.response;

import com.shopsphere.product.product.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto implements Serializable {
    private String id;
    private String name;
    private String sku;
    private Status status;
}
