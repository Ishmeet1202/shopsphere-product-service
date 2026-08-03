package com.shopsphere.product.product.service;

import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.response.PageResponseDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;

public interface ProductService {
    ProductResponseDto createProduct(ProductCreateRequestDto  request);

    ProductResponseDto getProductById(String id);

    PageResponseDto<ProductResponseDto> getAllProducts(
            Integer page,
            Integer size,
            String sortBy,
            String direction
    );

    ProductResponseDto updateProduct(String id, ProductCreateRequestDto request);

    void deleteProduct(String id);
}
