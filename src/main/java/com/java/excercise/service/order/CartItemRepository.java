package com.java.excercise.service.order;

import com.java.excercise.model.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

interface CartItemRepository extends JpaRepository<CartItem, String> {
}
