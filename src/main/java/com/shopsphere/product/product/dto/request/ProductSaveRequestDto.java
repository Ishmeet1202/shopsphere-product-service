package com.shopsphere.product.product.dto.request;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSaveRequestDto {
    @NotBlank(message = "Product name cannot be blank.")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters.")
    private String name;

    private String description;

    @NotBlank(message = "Product brand cannot be blank.")
    private String brand;

    @NotBlank(message = "Product category cannot be blank.")
    private String category;

    @NotNull(message = "Product price cannot be null.")
    @Positive(message = "Product price cannot be negative or zero.")
    private Double price;

    @NotNull(message = "Product quantity cannot be null.")
    @PositiveOrZero(message = "Product quantity cannot be negative.")
    private Integer quantity;

    @NotBlank(message = "Currency cannot be blank.")
    private String currency;
}
