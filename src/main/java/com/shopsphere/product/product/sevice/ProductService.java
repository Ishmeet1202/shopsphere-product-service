package com.shopsphere.product.product.sevice;

import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;

public interface ProductService {
    ProductResponseDto createProduct(ProductCreateRequestDto  request);
}
