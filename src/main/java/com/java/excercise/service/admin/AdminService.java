package com.java.excercise.service.admin;

import com.java.excercise.controller.product.ListOrderResponse;
import com.java.excercise.dto.admin.AllUsersResponse;
import com.java.excercise.dto.admin.UpdateStatusRequest;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.User;
import com.java.excercise.model.entities.UserInfo;
import com.java.excercise.model.enums.UserStatus;
import com.java.excercise.repository.CartRepository;
import com.java.excercise.repository.OrderRepository;
import com.java.excercise.repository.ProductRepository;
import com.java.excercise.repository.UserRepository;
import com.java.excercise.service.product.DetailService;
import com.java.excercise.service.product.ImageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final ImageService imageService;
    private final DetailService detailService;
    private final OrderRepository orderRepo;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Transactional(readOnly = true)
    public Page<AllUsersResponse> getAllUsers(String q, Pageable pageable) {
        Page<User> userPage;
        boolean hasQuery = q != null && !q.trim().isEmpty();

        if (hasQuery) {
            // 2. Nếu có 'q', tìm theo email hoặc name
            userPage = userRepo.findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(q, q, pageable);
        } else {
            // 1. Nếu không có 'q', lấy tất cả (phân trang)
            userPage = userRepo.findAll(pageable);
        }

        // 3. Map Page<User> sang Page<AllUsersResponse>
        return userPage.map(user -> {
            // Lấy userInfo một cách an toàn
            UserInfo info = user.getUserInfo();
            String avatarUrl = (info != null) ? info.getAvtUrl() : null; // Kiểm tra null

            return AllUsersResponse.builder()
                .userId(user.getId()) // Bạn đã thêm cái này, rất tốt
                .email(user.getEmail())
                .name(user.getName())
                .status(user.getStatus())
                .url(avatarUrl) // Gán avatarUrl vào đây
                .build();
        });
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Transactional // Rất quan trọng: Phải là Transactional
    public void deleteUser(String id) {
        // 1. Tìm User
        User user = userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));

        // 2. Xoá Giỏ hàng (Cart)
        // (Phải xoá trước vì Cart có FK đến User và không có cascade)
        cartRepo.findCartByUser(user).ifPresent(cartRepo::delete);

        // 3. Xoá Products và các tài nguyên liên quan (Cloudinary)
        List<Product> products = productRepo.findAllByUserId(user.getId());
        for (Product product : products) {
            // 3.1. Xoá ảnh trên Cloudinary (và DB)
            imageService.deleteImagesByProduct(product);

            // 3.2. Xoá ProductDetail
            detailService.deleteDetailByProduct(product);

            // 3.3. Xoá Product (JPA sẽ làm việc này qua cascade,
            // nhưng để chắc chắn, ta có thể xoá ở đây)
            // productRepo.delete(product);
            // --> Tạm thời không cần, vì cascade của User sẽ xử lý
        }

        // 4. Xoá User
        // Việc này sẽ tự động cascade xoá:
        // - UserInfo (vì @OneToOne(mappedBy="user", cascade=ALL))
        // - user_roles (vì @ElementCollection)
        // - Products (vì @OneToMany(mappedBy="user", cascade=ALL))
        //   -> và cascade tiếp xoá ProductImage (nhưng ta đã làm ở bước 3.1 rồi)
        userRepo.delete(user);

        // 5. (Không bắt buộc) Xoá RedisToken:
        // Các token trong Redis sẽ tự hết hạn (TTL).
        // Schema RedisToken của bạn (ID là jwtID) không cho phép tìm theo userID
        // một cách hiệu quả, nên việc để chúng tự hết hạn là chấp nhận được.
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Transactional
    public void updateState(String id, UpdateStatusRequest status) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));
        user.setStatus(status.getStatus());
        userRepo.save(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Transactional(readOnly = true)
    public List<ListOrderResponse> getAllOrders() {
        // 1. Lấy TẤT CẢ order
        var orders = orderRepo.findAll();

        // 2. Map sang kiểu ListOrderResponse y hệt như hàm getAllByUser
        // Dùng stream cho nó giống hàm getOrder() của ông
        return orders.stream()
            .map(order -> ListOrderResponse.builder()
                .order(order)
                .userId(order.getUser().getId()) // Lấy userId
                .build())
            .collect(Collectors.toList());
    }
}
