package com.java.excercise.controller.payment;

import com.java.excercise.controller.order.OrderItemResponse;
import com.java.excercise.controller.order.OrderResponse;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.Order;
import com.java.excercise.model.enums.OrderStatus;
import com.java.excercise.model.enums.PaymentMethod;
import com.java.excercise.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/checkouts")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderRepository orderRepo;

    @PostMapping("/{orderId}")
    public OrderResponse checkoutOrder(@PathVariable String orderId,
                                       @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        // Lấy order
        Order order = orderRepo.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new NotFoundException("Order not found", "ORDER_NOT_FOUND"));

        // Chỉ cho checkout nếu đang PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new NotFoundException("Order already paid or cancelled", "ORDER_ALREADY_CHECKED_OUT");
        }

        // Simulate thanh toán thành công
        order.setStatus(OrderStatus.COMPLETED); // hoặc COMPLETED nếu bạn muốn
        orderRepo.save(order);

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems()
            .stream()
            .map(oi -> new OrderItemResponse(oi.getProductId(), oi.getQuantity()))
            .toList();

        return new OrderResponse(
            order.getId(),
            order.getStatus(),
            order.getAmount(),
            order.getMethod(),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            items
        );
    }
}
