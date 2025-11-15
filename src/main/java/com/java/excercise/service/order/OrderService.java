package com.java.excercise.service.order;

import com.java.excercise.controller.order.OrderItemRequest;
import com.java.excercise.controller.order.OrderItemResponse;
import com.java.excercise.controller.order.OrderRequest;
import com.java.excercise.controller.order.OrderResponse;
import com.java.excercise.controller.product.ListOrderResponse;
import com.java.excercise.exception.InvalidException;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.*;
import com.java.excercise.model.enums.OrderStatus;
import com.java.excercise.repository.*;
import com.java.excercise.service.product.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepo;
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepo;
    private final ImageRepository imageRepo;
    private final ImageService imageService;

    @Transactional
    public List<ListOrderResponse> getAllByUser(User user) {
        var orders = orderRepo.findAllByUser(user);
        List<ListOrderResponse> listOrderResponse = new ArrayList<>();
        orders.forEach(order -> {
            String userId = order.getUser().getId();
            ListOrderResponse response = ListOrderResponse.builder()
                .order(order)
                .userId(userId)
                .build();
            listOrderResponse.add(response);
        });
        return listOrderResponse;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(String id, String userId) {
        Optional<Order> result = orderRepo.findByIdAndUserId(id, userId);
        if (result.isEmpty())
            throw new NotFoundException("Order not found", "ORDER_NOT_FOUND");

        Order order = result.get();
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        response.setAmount(order.getAmount());
        response.setMethod(order.getMethod());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
            .map(item -> {
                OrderItemResponse itemResp = new OrderItemResponse();
                Product product = productRepo.findById(item.getProductId()).get();

                // 2. Dùng 'product' đã lấy
                itemResp.setUserId(product.getUser().getId());
                itemResp.setProductId(item.getProductId());
                itemResp.setProductName(product.getName());
                List<String> images = imageService.getImagesByProduct(product);
                if (images != null && !images.isEmpty()) {
                    itemResp.setProductImg(images.get(0));
                } else {
                    itemResp.setProductImg(null); // Hoặc ảnh mặc định
                }
                itemResp.setPrice(product.getPrice());
                itemResp.setQuantity(item.getQuantity());

                return itemResp;
            })
            .toList();

        response.setItems(itemResponses);

        return response;
    }

    @Transactional
    public Order createOrder(User user, Cart cart, OrderRequest request) {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty())
            throw new InvalidException("Giỏ hàng trống", "CART_EMPTY");

        List<CartItem> cartItems = cart.getItems();

        // Danh sách để lưu các OrderItem sẽ được tạo
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequest req : request.getItems()) {
            CartItem cartItem = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(req.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại trong giỏ hàng", "ITEM_NOT_FOUND"));

            // Kiểm tra số lượng có bị thay đổi không (tránh trường hợp người dùng sửa frontend)
            if (!cartItem.getQuantity().equals(req.getQuantity()))
                throw new InvalidException("Số lượng sản phẩm đã thay đổi, vui lòng tải lại giỏ hàng", "INVALID_ITEM_QUANTITY");


            // Kiểm tra số lượng tồn kho (nếu cần thiết, tùy nghiệp vụ)
            Product product = cartItem.getProduct();
//            if (product.getStock() < req.getQuantity())
//                throw new InvalidException("Sản phẩm " + product.getName() + " không đủ hàng", "INSUFFICIENT_STOCK");


            // Tạo OrderItem
            OrderItem orderItem = OrderItem.builder()
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .price(product.getPrice()) // Giá tại thời điểm đặt hàng
                .build();

            orderItems.add(orderItem);

            // Cộng dồn tổng tiền
            totalAmount = totalAmount + (orderItem.getPrice() * orderItem.getQuantity());
        }

        // Tạo Order
        Order order = Order.builder()
            .user(user)
            .orderItems(orderItems)
            .amount(totalAmount)
            .method(request.getPaymentMethod())
            .note(request.getNote() != null ? request.getNote().trim() : null)
            .build();

        // Gán quan hệ 2 chiều (nếu OrderItem có trường Order)
        orderItems.forEach(item -> item.setOrder(order));

        // Lưu order vào DB
        Order savedOrder = orderRepo.save(order);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        cartItemRepository.deleteAll(cartItems);
        cart.getItems().clear();

        return savedOrder;
    }
}
