package com.shopsphere.product.product.entity;

import com.shopsphere.product.product.enums.Status;
import jakarta.persistence.*;


@Entity
@Table(name = "products")
public class Product extends AuditBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String brand;
    private String category;
    private Double price;
    private Integer quantity;
    private String currency;
    private String sku;
    private Status status;
}
