package com.java.excercise.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

/**
 * Phương thức này nhận vào một MultipartFile (file ảnh từ FE)
 * và upload nó lên Cloudinary.
 *
 * @param file File ảnh người dùng gửi lên
 * @return URL của ảnh sau khi upload
 * @throws IOException Ném lỗi nếu upload thất bại
 */
    public String uploadFile(MultipartFile file) throws IOException {
        // 1. Kiểm tra file
        if (file.isEmpty()) {
            throw new IOException("File rỗng, không thể upload.");
        }

        // 2. Cài đặt các tùy chọn upload
        Map<String, Object> options = ObjectUtils.asMap(
            // Tự động phát hiện kiểu file (image, video, raw)
            "resource_type", "auto",
            // Tạo một public_id ngẫu nhiên để tránh trùng tên file
            "public_id", UUID.randomUUID().toString(),
            "folder", "javaSpring"
        );

        // 3. Gọi API upload
        // file.getBytes() sẽ lấy dữ liệu file dưới dạng mảng byte
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

        // 4. Lấy URL an toàn (https) từ kết quả trả về
        // Kết quả trả về là một Map, 'secure_url' là key chứa link ảnh
        return uploadResult.get("secure_url").toString();
    }
}
