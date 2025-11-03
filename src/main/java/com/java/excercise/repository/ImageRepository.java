package com.java.excercise.repository;

import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByProduct(Product product);

    List<ProductImage> findAllByProduct(Product product);
}
