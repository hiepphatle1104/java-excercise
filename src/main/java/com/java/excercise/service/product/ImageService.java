package com.java.excercise.service.product;

import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductImage;
import com.java.excercise.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public void createImage(ProductImage productImage) {
        imageRepository.save(productImage);
    }

    public List<String> getImagesByProduct(Product product) {
        var images = imageRepository.findAllByProduct(product);

        return images.stream().map(ProductImage::getUrl).toList();
    }

    public void deleteImagesByProduct(Product product) {
        imageRepository.deleteProductImageByProduct(product);
    }

    @Transactional
    public List<String> updateImage(Product product, List<String> request) {
        List<ProductImage> images = imageRepository.findAllByProduct(product);
        if (!request.isEmpty())
            imageRepository.deleteAll(images);

        for (String url : request)
            imageRepository.save(new ProductImage(url, product));

        return imageRepository.findAllByProduct(product).stream().map(ProductImage::getUrl).toList();
    }
}
