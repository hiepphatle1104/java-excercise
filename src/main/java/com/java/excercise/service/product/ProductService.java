package com.java.excercise.service.product;

import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.Product;
import com.java.excercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        Optional<Product> result = productRepository.findById(id);
        if (result.isEmpty())
            throw new NotFoundException("product not found", "PRODUCT_NOT_FOUND");

        return result.get();
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(String id, Product product) {
        return null;
    }

    public List<Product> findAllByUserId(String userId) {

        List<Product> productList = productRepository.findAllByUserId(userId);
        if(productList.isEmpty())
            throw new NotFoundException("product not found", "PRODUCTS_NOT_FOUND");
        return productList;
    }
}
