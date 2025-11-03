package com.java.excercise.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JoinColumn(name = "product_id")
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Product product;

    private String batteryPercentage;
    private String motorCapacity;
    private String maximumDistance;
    private String chargingTime;
    private String weight;
}
