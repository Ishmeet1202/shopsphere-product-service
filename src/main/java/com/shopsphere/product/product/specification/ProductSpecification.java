package com.shopsphere.product.product.specification;

import com.shopsphere.product.product.dto.request.ProductSearchRequestDto;
import com.shopsphere.product.product.entity.Product;
import com.shopsphere.product.tenant.context.TenantContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filter(ProductSearchRequestDto searchRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            String tenantId = TenantContext.requireTenantId();
            predicates.add(cb.equal(root.get("tenantId"), tenantId));

            if (StringUtils.hasText(searchRequest.getName())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + searchRequest.getName().toLowerCase() + "%"
                        )
                );
            }

            if (StringUtils.hasText(searchRequest.getBrand())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("brand")),
                                "%" + searchRequest.getBrand().toLowerCase() + "%"
                        )
                );
            }

            if (StringUtils.hasText(searchRequest.getCategory())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("category")),
                                "%" + searchRequest.getCategory().toLowerCase() + "%"
                        )
                );
            }

            if (searchRequest.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), searchRequest.getStatus())
                );
            }

            predicates.add(cb.isFalse(root.get("deleted")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
