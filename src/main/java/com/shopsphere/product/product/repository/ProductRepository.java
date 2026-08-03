package com.shopsphere.product.product.repository;

import com.shopsphere.product.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByIdAndIsDeletedFalse(String id);
    Page<Product> findAllAndIsDeletedFalse(Pageable pageable);
}
