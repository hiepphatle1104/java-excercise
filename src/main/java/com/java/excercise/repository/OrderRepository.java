package com.java.excercise.repository;

import com.java.excercise.model.entities.Order;
import com.java.excercise.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByUser(User user);

    Optional<Order> findByIdAndUserId(String id, String userId);
}
