package com.java.excercise.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String url;

    @Column(nullable = false)
    private String publicId;

    @JoinColumn(name = "product_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Product product;

    public ProductImage(String url, String publicId, Product product) {
        this.url = url;
        this.product = product;
        this.publicId = publicId;
    }
}
