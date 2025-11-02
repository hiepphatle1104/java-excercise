package com.swappie.testproduct.repository;

import com.swappie.testproduct.model.Product;
import com.swappie.testproduct.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByProduct(Product product);

    List<ProductImage> findAllByProduct(Product product);
}
