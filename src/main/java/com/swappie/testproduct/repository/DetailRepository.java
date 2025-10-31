package com.swappie.testproduct.repository;

import com.swappie.testproduct.model.Product;
import com.swappie.testproduct.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailRepository extends JpaRepository<ProductDetail, Long> {
    Optional<ProductDetail> findByProduct(Product product);
}
