package com.shopsphere.product.product.sevice.impl;

import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;
import com.shopsphere.product.product.entity.Product;
import com.shopsphere.product.product.enums.Status;
import com.shopsphere.product.product.repository.ProductRepository;
import com.shopsphere.product.product.sevice.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto request) {
        if (request == null) {
            throw new RuntimeException("Request is null !");
        }

        Product product = toProductEntity(request);

        product = productRepository.save(product);

        return toProductResponseDto(product);
    }

    private Product toProductEntity(ProductCreateRequestDto request) {
        return Product.builder()
                .name(request.getName().trim())
                .description(
                        request.getDescription() != null && !request.getDescription().isBlank()
                                ? request.getDescription()
                                : ""
                )
                .brand(request.getBrand().trim())
                .category(request.getCategory().trim())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .status(
                        request.getQuantity() > 0 ? Status.ACTIVE : Status.OUT_OF_STOCK
                )
                .sku(generateSku())
                .build();
    }

    private ProductResponseDto toProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .status(product.getStatus())
                .build();
    }

    private String generateSku() {
        String random = UUID.randomUUID().toString().substring(0,8);
        return "PRD-" + random;
    }
}
