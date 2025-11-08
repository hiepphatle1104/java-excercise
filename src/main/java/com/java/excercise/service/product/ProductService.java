package com.java.excercise.service.product;

import com.java.excercise.dto.product.UpdateProductRequest;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.Product;
import com.java.excercise.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository repo;

    public Product createProduct(Product product) {
        return repo.save(product);
    }

//    public List<Product> getAllProducts() {
//        return repo.findAll();
//    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Product> searchProducts(String query, Pageable pageable) {
        return repo.findByNameContainingIgnoreCase(query, pageable);
    }

    public Product getProductById(String id) {
        Optional<Product> result = repo.findById(id);
        if (result.isEmpty())
            throw new NotFoundException("product not found", "PRODUCT_NOT_FOUND");

        return result.get();
    }

    public void deleteProduct(String id) {
        repo.deleteById(id);
    }

    @Transactional
    public Product updateProduct(String id, UpdateProductRequest request) {
        Product product = getProductById(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setStatus(request.getStatus());
        product.setBrand(request.getBrand());
        product.setDate(request.getDate());
        product.setPrice(request.getPrice());

        return repo.save(product);
    }

    public List<Product> findAllByUserId(String userId) {

        List<Product> productList = repo.findAllByUserId(userId);
        if(productList.isEmpty())
            throw new NotFoundException("product not found", "PRODUCTS_NOT_FOUND");
        return productList;
    }
}
