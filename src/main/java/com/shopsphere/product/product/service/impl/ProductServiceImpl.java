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
import com.shopsphere.product.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final static String PRODUCT_NOT_FOUND_MESSAGE = "Product not found with id: ";
    private final static String INVALID_SORTING_FIELD_MESSAGE = "Invalid sorting field: ";
    private final static Set<String> ALLOWED_SORTING_FIELDS = Set.of("name", "status");

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto request) {
        String tenantId = TenantContext.getCurrentTenant();
        LOGGER.info("createProduct: name={}, brand={}, category={}, tenantId={}", request.getName(), request.getBrand(), request.getCategory(), tenantId);
        Product product = productMapper.toProductEntity(request);

        initializeNewProduct(product, tenantId);

        product = productRepository.save(product);
        LOGGER.info("createProduct: saved product id={}, sku={}, tenantId={}", product.getId(), product.getSku(), tenantId);
        return productMapper.toProductResponseDto(product);
    }

    @Override
    @Cacheable(
            cacheNames = "products",
            key = "T(com.shopsphere.product.tenant.cache.TenantCacheKey).product(#id)"
    )
    public ProductResponseDto getProductById(String id) {
        String tenantId = TenantContext.getCurrentTenant();
        Product product = productRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id));

        LOGGER.info("Product info fetched for id: {}, tenantId: {}", id, tenantId);

        return productMapper.toProductResponseDto(product);
    }

    @Override
    public PageResponseDto<ProductResponseDto> getAllProducts(
            Integer page,
            Integer size,
            String sortBy,
            String direction
    ) {
        String tenantId = TenantContext.getCurrentTenant();
        LOGGER.info("getAllProducts: page={}, size={}, sortBy={}, direction={}, tenantId={}", page, size, sortBy, direction, tenantId);
        String fieldName = sortBy.toLowerCase();

        if (!ALLOWED_SORTING_FIELDS.contains(fieldName)) {
            LOGGER.warn("Invalid sorting field requested: {}", fieldName);
            throw new InvalidSortingFieldException(INVALID_SORTING_FIELD_MESSAGE + fieldName);
        }

        Pageable pageable = direction.equalsIgnoreCase("desc")
                ? PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, fieldName))
                : PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, fieldName));

        Page<Product> products = productRepository.findByTenantIdAndDeletedFalse(tenantId, pageable);
        Page<ProductResponseDto> productResponseDtoList = products.map(productMapper::toProductResponseDto);
        LOGGER.info("getAllProducts: found {} totalElements={}, totalPages={}", productResponseDtoList.getNumberOfElements(), productResponseDtoList.getTotalElements(), productResponseDtoList.getTotalPages());
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
        String tenantId = TenantContext.getCurrentTenant();
        LOGGER.info("searchProducts: name={}, page={}, size={}, sortBy={}, direction={}, tenantId={}", searchRequest.getName(), searchRequest.getPage(), searchRequest.getSize(), searchRequest.getSortBy(), searchRequest.getDirection(), tenantId);
        String fieldName = searchRequest.getSortBy().toLowerCase();

        if (!ALLOWED_SORTING_FIELDS.contains(fieldName)) {
            LOGGER.warn("Invalid sorting field in search: {}", fieldName);
            throw new InvalidSortingFieldException(INVALID_SORTING_FIELD_MESSAGE + fieldName);
        }

        Pageable pageable = searchRequest.getDirection().equalsIgnoreCase("desc")
                ? PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by(Sort.Direction.DESC, fieldName))
                : PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), Sort.by(Sort.Direction.ASC, fieldName));

        Specification<Product> productSpecification = ProductSpecification.filter(searchRequest);

        Page<Product> products = productRepository.findAll(productSpecification, pageable);
        Page<ProductResponseDto> productResponsePages = products.map(productMapper::toProductResponseDto);
        LOGGER.info("searchProducts: returned {} items, totalElements={}", productResponsePages.getNumberOfElements(), productResponsePages.getTotalElements());
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
    @CacheEvict(
            cacheNames = "products",
            key = "T(com.shopsphere.product.tenant.cache.TenantCacheKey).product(#id)"
    )
    public ProductResponseDto updateProduct(String id, ProductCreateRequestDto request) {
        String tenantId = TenantContext.getCurrentTenant();
        LOGGER.info("updateProduct called id={}, name={}, tenantId={}", id, request.getName(), tenantId);
        Product product = productRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id));

        productMapper.updateProductEntity(product, request);

        updateProductStatus(product);

        product = productRepository.save(product);
        LOGGER.info("updateProduct saved id={}, status={}, tenantId={}", product.getId(), product.getStatus(), tenantId);
        return productMapper.toProductResponseDto(product);
    }

    @Override
    @CacheEvict(
            cacheNames = "products",
            key = "T(com.shopsphere.product.tenant.cache.TenantCacheKey).product(#id)"
    )
    @Transactional
    public void deleteProduct(String id) {
        String tenantId = TenantContext.getCurrentTenant();
        LOGGER.info("deleteProduct called id={}, tenantId={}", id, tenantId);
        Product product = productRepository.findByIdAndTenantIdAndDeletedFalse(id, tenantId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id));

        product.setDeleted(true);

        productRepository.save(product);
        LOGGER.info("deleteProduct completed id={}, tenantId={}", id, tenantId);
    }


    private void initializeNewProduct(Product product, String tenantId) {
        product.setSku(generateSku());
        product.setDeleted(false);
        product.setTenantId(tenantId);
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
