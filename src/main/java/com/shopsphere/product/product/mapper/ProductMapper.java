package com.shopsphere.product.product.mapper;

import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;
import com.shopsphere.product.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProductEntity(ProductCreateRequestDto request) {
        return Product.builder()
                .name(request.getName().trim())
                .description(request.getDescription().trim())
                .brand(request.getBrand().trim())
                .category(request.getCategory().trim())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
    }

    public ProductResponseDto toProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .status(product.getStatus())
                .build();
    }
}
