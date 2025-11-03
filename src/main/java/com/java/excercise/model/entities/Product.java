package com.java.excercise.model.entities;

import com.java.excercise.model.enums.ProductCategory;
import com.java.excercise.model.enums.ProductStatus;
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

    private String userId;

    private String name;
    private String brand;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private String description;
    private Double price; //them price
    private String date;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
}
