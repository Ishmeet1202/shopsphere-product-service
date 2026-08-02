package com.shopsphere.product.product.controller;

import com.shopsphere.product.product.dto.request.ProductCreateRequestDto;
import com.shopsphere.product.product.dto.response.ProductResponseDto;
import com.shopsphere.product.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
