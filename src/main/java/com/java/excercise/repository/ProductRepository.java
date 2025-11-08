package com.java.excercise.repository;

import com.java.excercise.model.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByUserId(String userId);

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String nameKey, Pageable pageable);
}
