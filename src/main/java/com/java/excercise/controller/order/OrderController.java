package com.java.excercise.controller.order;

import com.java.excercise.dto.ApiResponse;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.model.entities.*;
import com.java.excercise.repository.CartRepository;
import com.java.excercise.repository.OrderRepository;
import com.java.excercise.repository.UserRepository;
import com.java.excercise.service.auth.UserService;
import com.java.excercise.service.cart.CartService;
import com.java.excercise.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getSubject();
        User user = userService.getUserById(id);

        var res = orderService.getAllByUser(user);
        return new ResponseEntity<>(ApiResponse.success("get all event", res), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId, @AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getSubject();

        var res = orderService.getOrder(orderId, id);
        return new ResponseEntity<>(ApiResponse.success("get event", res), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request, @AuthenticationPrincipal Jwt jwt) {
        String id = jwt.getSubject();
        User user = userService.getUserById(id);

        Cart cart = cartService.getCart(user);

        var res = orderService.createOrder(user, cart, request);

        return new ResponseEntity<>(ApiResponse.success("create success", res), HttpStatus.CREATED);
    }
}
