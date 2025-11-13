package com.java.excercise.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // 1. Quan hệ MỘT-MỘT với User
    // Một giỏ hàng chỉ thuộc về 1 User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // 2. Quan hệ MỘT-NHIỀU với CartItem
    // Một giỏ hàng có thể chứa nhiều món hàng (CartItem)
    @OneToMany(
        mappedBy = "cart",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    @Builder.Default // Khi dùng @Builder, tự khởi tạo list này
    private List<CartItem> items = new ArrayList<>();
}
