package com.java.excercise.repository;

import com.java.excercise.dto.cart.CartItemDTO;
import com.java.excercise.model.entities.Cart;
import com.java.excercise.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findCartByUser(User user);
}

