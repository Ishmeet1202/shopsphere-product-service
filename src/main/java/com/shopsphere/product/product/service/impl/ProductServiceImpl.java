package com.shopsphere.product.product.service.impl;

import com.shopsphere.product.exception.InvalidSortingFieldException;
import com.shopsphere.product.exception.ProductNotFoundException;
import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.request.ProductSearchRequestDto;
import com.shopsphere.product.product.dto.response.PageResponseDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;
import com.shopsphere.product.product.entity.Product;
import com.shopsphere.product.product.enums.Status;
import com.shopsphere.product.product.mapper.ProductMapper;
import com.shopsphere.product.product.repository.ProductRepository;
import com.shopsphere.product.product.service.ProductService;
import com.shopsphere.product.product.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final static String PRODUCT_NOT_FOUND_MESSAGE = "Product not found with id: ";
    private final static String INVALID_SORTING_FIELD_MESSAGE = "Invalid sorting field: ";
    private final static Set<String> ALLOWED_SORTING_FIELDS = Set.of("name", "status");

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
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id));

        return productMapper.toProductResponseDto(product);
    }

    @Override
    public PageResponseDto<ProductResponseDto> getAllProducts(
            Integer page,
            Integer size,
            String sortBy,
            String direction
    ) {

        String fieldName = sortBy.toLowerCase();

        if (!ALLOWED_SORTING_FIELDS.contains(fieldName)) {
            throw new InvalidSortingFieldException(INVALID_SORTING_FIELD_MESSAGE + fieldName);
        }

        Pageable pageable = direction.equalsIgnoreCase("desc")
                ? PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, fieldName))
                : PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, fieldName));

        Page<Product> products = productRepository.findByDeletedFalse(pageable);
        Page<ProductResponseDto> productResponseDtoList = products.map(productMapper::toProductResponseDto);

        return PageResponseDto.<ProductResponseDto>builder()
                .content(productResponseDtoList.getContent())
                .page(productResponseDtoList.getNumber())
                .size(productResponseDtoList.getSize())
                .totalElements(productResponseDtoList.getTotalElements())
                .totalPages(productResponseDtoList.getTotalPages())
                .last(productResponseDtoList.isLast())
                .build();
    }

    @Override
    public PageResponseDto<ProductResponseDto> searchProducts(ProductSearchRequestDto searchRequest) {
        String fieldName = searchRequest.getSortBy().toLowerCase();

        if (!ALLOWED_SORTING_FIELDS.contains(fieldName)) {
            throw new InvalidSortingFieldException(INVALID_SORTING_FIELD_MESSAGE + fieldName);
        }

        Pageable pageable = searchRequest.getDirection().equalsIgnoreCase("desc")
                ? PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by(Sort.Direction.DESC, fieldName))
                : PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by(Sort.Direction.ASC, fieldName));

        Specification<Product> productSpecification = ProductSpecification.filter(searchRequest);

        Page<Product> products = productRepository.findAll(productSpecification, pageable);
        Page<ProductResponseDto> productResponsePages = products.map(productMapper::toProductResponseDto);

        return PageResponseDto.<ProductResponseDto>builder()
                .content(productResponsePages.getContent())
                .page(productResponsePages.getNumber())
                .size(productResponsePages.getSize())
                .totalElements(productResponsePages.getTotalElements())
                .totalPages(productResponsePages.getTotalPages())
                .last(productResponsePages.isLast())
                .build();
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(String id, ProductCreateRequestDto request) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id));

        productMapper.updateProductEntity(product, request);

        updateProductStatus(product);

        product = productRepository.save(product);

        return productMapper.toProductResponseDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id));

        product.setDeleted(true);

        productRepository.save(product);
    }


    private void initializeNewProduct(Product product) {
        product.setSku(generateSku());
        product.setDeleted(false);
        updateProductStatus(product);
    }

    private void updateProductStatus(Product product) {
        product.setStatus(
                product.getQuantity() > 0 ? Status.ACTIVE : Status.OUT_OF_STOCK
        );
    }

    private String generateSku() {
        String random = UUID.randomUUID().toString().substring(0,8);
        return "PRD-" + random;
    }
}
