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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable(name = "id") String id
    ) {
      return ResponseEntity.status(HttpStatus.OK)
              .body(productService.getProductById(id));
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
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts(
                        page,
                        size,
                        sortBy,
                        direction
                ));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<ProductResponseDto>> searchProducts(
            @Valid ProductSearchRequestDto searchRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.searchProducts(
                        searchRequest
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody ProductCreateRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(
                        id, request
                ));
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
