package com.shopsphere.product.product.service.impl;

import com.shopsphere.product.exception.ProductNotFoundException;
import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;
import com.shopsphere.product.product.entity.Product;
import com.shopsphere.product.product.enums.Status;
import com.shopsphere.product.product.mapper.ProductMapper;
import com.shopsphere.product.product.repository.ProductRepository;
import com.shopsphere.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final static String PRODUCT_NOT_FOUND_MESSAGE = "Product not found with id: ";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto request) {
        Product product = productMapper.toProductEntity(request);

        initializeNewProduct(product);

        product = productRepository.save(product);

        return productMapper.toProductResponseDto(product);
    }

    @Override
    public ProductResponseDto getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id));

        return productMapper.toProductResponseDto(product);
    }


    private void initializeNewProduct(Product product) {
        product.setStatus(
                product.getQuantity() > 0 ? Status.ACTIVE : Status.OUT_OF_STOCK
        );
        product.setSku(generateSku());
    }

    private String generateSku() {
        String random = UUID.randomUUID().toString().substring(0,8);
        return "PRD-" + random;
    }
}
