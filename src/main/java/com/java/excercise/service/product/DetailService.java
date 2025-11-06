package com.java.excercise.service.product;

import com.java.excercise.dto.product.ProductDetailRequest;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductDetail;
import com.java.excercise.repository.DetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetailService {
    private final DetailRepository detailRepository;

    public void createDetail(ProductDetail productDetail) {
        detailRepository.save(productDetail);
    }

    public ProductDetail getDetailByProduct(Product product) {
        Optional<ProductDetail> result = detailRepository.findByProduct(product);
        if (result.isEmpty())
            throw new NotFoundException("product detail not found", "PRODUCT_DETAIL_NOT_FOUND");

        return result.get();
    }

    public void deleteDetailByProduct(Product product) {
        detailRepository.deleteProductDetailByProduct(product);
    }

    @Transactional
    public ProductDetail updateDetail(Product product, ProductDetailRequest request) {
        ProductDetail detail = getDetailByProduct(product);

        detail.setBatteryPercentage(request.batteryPercentage());
        detail.setMaximumDistance(request.maximumDistance());
        detail.setWeight(request.weight());
        detail.setChargingTime(request.chargingTime());
        detail.setMotorCapacity(request.motorCapacity());

        return detailRepository.save(detail);
    }
}
