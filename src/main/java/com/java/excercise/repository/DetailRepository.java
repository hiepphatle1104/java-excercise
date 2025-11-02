package com.java.excercise.repository;

import com.java.excercise.model.Product;
import com.java.excercise.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailRepository extends JpaRepository<ProductDetail, Long> {
    Optional<ProductDetail> findByProduct(Product product);
}
