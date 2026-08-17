package com.shopsphere.product.product.controller;

import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.request.ProductSearchRequestDto;
import com.shopsphere.product.product.dto.response.PageResponseDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;
import com.shopsphere.product.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductCreateRequestDto request) {
        ProductResponseDto response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable(name = "id") String id
    ) {
        ProductResponseDto response = productService.getProductById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponseDto>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0")
            @PositiveOrZero(message = "Page cannot be negative.")
            Integer page,
            @RequestParam(name = "size", defaultValue = "10")
            @Positive(message = "Size cannot be negative or zero.")
            @Max(value = 100, message = "Maximum page size is 100")
            Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {
        PageResponseDto<ProductResponseDto> response = productService.getAllProducts(
                page,
                size,
                sortBy,
                direction
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<ProductResponseDto>> searchProducts(
            @Valid ProductSearchRequestDto searchRequest
    ) {
        PageResponseDto<ProductResponseDto> response = productService.searchProducts(
                searchRequest
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody ProductCreateRequestDto request
    ) {
        ProductResponseDto response = productService.updateProduct(
                id, request
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(
            @PathVariable(name = "id") String id
    ) {
        productService.deleteProduct(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(
                        "message", "Product deleted successfully"
                ));
    }
}
