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
                .description(
                        request.getDescription() != null
                                ? request.getDescription()
                                : null
                )
                .brand(request.getBrand().trim())
                .category(request.getCategory().trim())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .currency(request.getCurrency())
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

    public void updateProductEntity(Product product, ProductCreateRequestDto request) {
        product.setName(request.getName());
        product.setDescription(
                request.getDescription() != null
                ? request.getDescription() : null
        );
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCurrency(request.getCurrency());
    }
}
