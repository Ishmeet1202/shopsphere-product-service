package com.shopsphere.product.product.specification;

import com.shopsphere.product.product.dto.request.ProductSearchRequestDto;
import com.shopsphere.product.product.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filter(ProductSearchRequestDto searchRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.getName() != null) {
                predicates.add(
                        cb.equal(root.get("name"), searchRequest.getName())
                );
            }

            if (searchRequest.getBrand() != null) {
                predicates.add(
                        cb.equal(root.get("brand"), searchRequest.getBrand())
                );
            }

            if (searchRequest.getName() != null) {
                predicates.add(
                        cb.equal(root.get("category"), searchRequest.getCategory())
                );
            }

            if (searchRequest.getName() != null) {
                predicates.add(
                        cb.equal(root.get("status"), searchRequest.getStatus())
                );
            }

            predicates.add(cb.isFalse(root.get("deleted")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
