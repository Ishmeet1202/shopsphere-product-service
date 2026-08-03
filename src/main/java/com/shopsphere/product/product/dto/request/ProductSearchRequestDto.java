package com.shopsphere.product.product.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSearchRequestDto {
    private String name;
    private String brand;
    private String category;
    private String status;

    @PositiveOrZero(message = "Page must be zero or positive")
    private Integer page = 0;

    @Positive(message = "Size must be greater than zero")
    @Max(value = 100, message = "Maximum page size is 100")
    private Integer size = 10;

    private String sortBy = "name";
    private String direction = "asc";
}
