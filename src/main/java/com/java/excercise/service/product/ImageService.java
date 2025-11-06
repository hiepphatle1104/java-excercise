package com.java.excercise.service.product;

import com.java.excercise.dto.product.CloudinaryResponse;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.ProductImage;
import com.java.excercise.repository.ImageRepository;
import com.java.excercise.repository.ProductRepository;
import com.java.excercise.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    public void createImage(ProductImage productImage) {
        imageRepository.save(productImage);
    }

    public List<String> getImagesByProduct(Product product) {
        var images = imageRepository.findAllByProduct(product);

        return images.stream().map(ProductImage::getUrl).toList();
    }

//    public void deleteImagesByProduct(Product product) {
//        imageRepository.deleteProductImageByProduct(product);
//    }
    /**
     * Xóa tất cả ảnh của sản phẩm (cả trên Cloudinary và DB)
     */
    @Transactional
    public void deleteImagesByProduct(Product product) {
        // 1. Lấy danh sách ảnh từ DB
        List<ProductImage> images = imageRepository.findAllByProduct(product);

        // 2. Xóa từng ảnh khỏi Cloudinary dùng publicId
        for (ProductImage image : images) {
            // Lấy publicId trực tiếp từ entity
            cloudinaryService.deleteFile(image.getPublicId());
        }

        // 3. Xóa tất cả ảnh khỏi DB
        imageRepository.deleteProductImageByProduct(product);
    }

//    @Transactional
//    public List<String> updateImage(Product product, List<String> request) {
//        List<ProductImage> images = imageRepository.findAllByProduct(product);
//        if (!request.isEmpty())
//            imageRepository.deleteAll(images);
//
//        for (String url : request)
//            imageRepository.save(new ProductImage(url, product));
//
//        return imageRepository.findAllByProduct(product).stream().map(ProductImage::getUrl).toList();
//    }

    /**
     * Hàm updateImage mới, khớp với logic FE:
     * - Nếu newImageFiles rỗng/null: Không làm gì cả, trả về ảnh cũ.
     * - Nếu newImageFiles có file: Xóa hết ảnh cũ, upload ảnh mới.
     */
    @Transactional
    public List<String> updateImage(Product product, List<MultipartFile> newImages) {
        // Case 1: FE không gửi file mới (images là null hoặc rỗng)
        // -> User không muốn đổi ảnh -> Trả về list ảnh hiện tại
        if (newImages == null || newImages.isEmpty()) {
            // trả về list ảnh cũ
            List<ProductImage> oldImages = imageRepository.findAllByProduct(product);
            return oldImages.stream()
                .map(ProductImage::getUrl)
                .toList();
        }

        // Case 2: FE gửi list file, nhưng tất cả đều rỗng
        // Lọc ra những file thực sự có nội dung
        List<MultipartFile> validFiles = newImages.stream()
            .filter(file -> file != null && !file.isEmpty())
            .toList();
        if (validFiles.isEmpty()) {
            // Vẫn trả về ảnh cũ
            return imageRepository.findAllByProduct(product)
                .stream()
                .map(ProductImage::getUrl)
                .toList();
        }

        // ---
        // Case 3: FE CÓ gửi file mới
        // -> Thực hiện logic "Thay thế toàn bộ"
        // ---
        // xóa toàn bộ ảnh trên cloudinary và db
        deleteImagesByProduct(product);

        // 2. Upload và lưu TẤT CẢ ảnh mới
        List<String> finalImageUrls = new ArrayList<>();
        for (MultipartFile image : validFiles) {
            try {
                // upload ảnh lên cloudinary
                CloudinaryResponse uploadData = cloudinaryService.uploadFile(image);
                // lưu ảnh vào db
                imageRepository.save(
                    new ProductImage(uploadData.url(), uploadData.publicId(), product)
                );
                finalImageUrls.add(uploadData.url());
            } catch (IOException e) {
                throw new RuntimeException("Upload ảnh thất bại", e);
            }
        }
        return finalImageUrls;
    }
}
