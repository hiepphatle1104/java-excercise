package com.swappie.testproduct.model;

import com.swappie.testproduct.enums.ProductCategory;
import com.swappie.testproduct.enums.ProductCondition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String brand;

    @Enumerated(EnumType.STRING)
    private ProductCondition condition;
    private String description;
    private String date;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
}
