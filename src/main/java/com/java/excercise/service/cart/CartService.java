package com.java.excercise.service.cart;

import com.java.excercise.dto.cart.AddCartItemRequest;
import com.java.excercise.dto.cart.CartItemDTO;
import com.java.excercise.dto.cart.CartResponse;
import com.java.excercise.exception.NotFoundException;
import com.java.excercise.mapper.CartMapper;
import com.java.excercise.model.entities.Cart;
import com.java.excercise.model.entities.CartItem;
import com.java.excercise.model.entities.Product;
import com.java.excercise.model.entities.User;
import com.java.excercise.repository.CartRepository;
import com.java.excercise.repository.ProductRepository;
import com.java.excercise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;

    @Transactional
    public CartResponse addItemToCart(String id, AddCartItemRequest addCartItemRequest) {
        // 1. Tìm user và product
        User user = userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));
        Product product = productRepo.findById(addCartItemRequest.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found", "PRODUCT_NOT_FOUND"));

        // 2. Lấy giỏ hàng user, nếu chưa có thì tạo mới
        Cart cart = cartRepo.findCartByUser(user)
            .orElseGet(() -> createCart(user));

        // 3. Xử lý item
        // kiểm tra xem sản phẩm có trong giỏ chưa
        Optional<CartItem> existedItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(addCartItemRequest.getProductId()))
            .findFirst();

        if (existedItem.isPresent()) {
            // nếu có thì lấy ra và cập nhật số lượng
            CartItem cartItemExisted = existedItem.get();
            cartItemExisted.setQuantity(cartItemExisted.getQuantity() + addCartItemRequest.getQuantity());
        } else {
            // nếu chưa thì tạo cart item mới
            CartItem newCartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(addCartItemRequest.getQuantity())
                .build();
            // add vào list cart item
            cart.getItems().add(newCartItem);
        }

        Cart save = cartRepo.save(cart);
        double total = total(save);

        CartResponse response = CartResponse.builder()
            .cartItem(mapCartToListDTO(save))
            .total(total)
            .build();
        return response;
    }

    public CartResponse getCartByUserId(String userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));

        Cart cart = cartRepo.findCartByUser(user)
            .orElse(null); // Không cần tạo mới nếu chỉ GET

        if (cart == null) {
            CartResponse response = CartResponse.builder()
                .cartItem(Collections.emptyList())
                .total(0)
                .build();
            return response; // Trả về list rỗng
        }

        CartResponse response = CartResponse.builder()
            .cartItem(mapCartToListDTO(cart))
            .total(total(cart))
            .build();
        return response;
    }

    private double total(Cart cart) {
        return cart.getItems().stream().mapToDouble(
            item -> {
                // Kiểm tra nếu price null, coi như là 0
                Double price = item.getProduct().getPrice();
                if (price == null) {
                    return 0.0;
                }
                return price * item.getQuantity();
            }
        ).sum();
    }

    public CartResponse deleteItemFromCart(String id, String productId) {
        // 1. tìm user
        User user =  userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));

        // 2. tìm giỏ hàng
        Cart cart = cartRepo.findCartByUser(user)
            .orElseThrow(() -> new NotFoundException("Cart not found", "CART_NOT_FOUND"));

        // 3. xử lý item
        boolean removed = cart.getItems().removeIf(
            item -> item.getProduct().getId().equals(productId)
        );
        if (!removed) {
            throw new NotFoundException("Product not found in cart", "PRODUCT_NOT_IN_CART");
        }
        // 4. save lại
        Cart save = cartRepo.save(cart);
        // 5. trả về cart mới nhất
        return CartResponse.builder()
            .cartItem(mapCartToListDTO(save))
            .total(total(save))
            .build();
    }

    @Transactional
    public CartResponse updateItemInCart(String id, AddCartItemRequest request) {
        // 1. tìm user
        User user = userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found", "USER_NOT_FOUND"));

        // 2. tìm giỏ hàng
        Cart cart = cartRepo.findCartByUser(user)
            .orElseThrow(() -> new NotFoundException("Cart not found", "CART_NOT_FOUND"));
        // 3. xử lý item
        CartItem itemToUpdate = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(request.getProductId()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Product not found in cart", "PRODUCT_NOT_IN_CART"));
        // 4. update
        itemToUpdate.setQuantity(request.getQuantity());
        cart = cartRepo.save(cart);
        return CartResponse.builder()
            .cartItem(mapCartToListDTO(cart))
            .total(total(cart))
            .build();
    }

    private List<CartItemDTO> mapCartToListDTO(Cart cart) {
        return cart.getItems().stream()
            .map(CartMapper::toCartItemDTO)
            .collect(Collectors.toList());
    }

    private Cart createCart(User user) {
        Cart newCart = Cart.builder()
            .user(user)
            .items(new ArrayList<>())
            .build();
        return cartRepo.save(newCart); // Lưu và trả về cart mới
    }
}
