package com.java.excercise.controller.order;

import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.*;
import com.java.excercise.repository.CartRepository;
import com.java.excercise.repository.OrderRepository;
import com.java.excercise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CartRepository cartRepo;
    private final UserRepository userRepo;
    private final OrderRepository orderRepo;

    @GetMapping
    public List<OrderResponse> getAllOrders(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));

        return orderRepo.findAllByUser(user)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId,
                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        Order order = orderRepo.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new NotFoundException("Order not found", "ORDER_NOT_FOUND"));

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems()
            .stream()
            .map(oi -> new OrderItemResponse(oi.getProductId(), oi.getQuantity()))
            .toList();

        return new OrderResponse(
            order.getId(),
            order.getAmount(),
            order.getMethod(),
            order.getStatus(),
            order.getCreatedAt(),
            items
        );
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        User user = userRepo.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));

        Cart cart = cartRepo.findCartByUser(user)
            .orElseThrow(() -> new NotFoundException("Cart not found", "CART_NOT_FOUND"));

        // Map productId -> requested quantity
        Map<String, Integer> requestMap = request.getItems()
            .stream()
            .collect(Collectors.toMap(OrderItemRequest::getId, OrderItemRequest::getQuantity));

        // Lọc các item trong cart theo request
        List<CartItem> filteredItems = cart.getItems()
            .stream()
            .filter(i -> requestMap.containsKey(i.getProduct().getId()))
            .toList();

        if (filteredItems.isEmpty())
            throw new NotFoundException("No matching items in cart", "ITEMS_NOT_FOUND");

        // Tạo Order
        Order order = Order.builder()
            .user(user)
            .method(request.getMethod())
            .build();

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (CartItem cartItem : filteredItems) {
            String productId = cartItem.getProduct().getId();
            int requestedQty = requestMap.get(productId);

            if (requestedQty > cartItem.getQuantity()) {
                throw new NotFoundException(
                    "Not enough quantity for product " + productId,
                    "INSUFFICIENT_QUANTITY"
                );
            }

            double itemAmount = requestedQty * cartItem.getProduct().getPrice();
            totalAmount += itemAmount;

            // Tạo OrderItem
            OrderItem orderItem = OrderItem.builder()
                .order(order)
                .productId(productId)
                .quantity(requestedQty)
                .build();

            orderItems.add(orderItem);

            // Update cart
            if (cartItem.getQuantity() == requestedQty) {
                cart.getItems().remove(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() - requestedQty);
            }
        }


        order.setAmount(totalAmount);
        order.setOrderItems(orderItems);

        // Lưu order + orderItems
        orderRepo.save(order);

        // Lưu cart sau khi update
        cartRepo.save(cart);

        return toResponse(order);
    }
}
