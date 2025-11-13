package com.java.excercise.mapper;

import com.java.excercise.dto.cart.CartItemDTO;
import com.java.excercise.model.entities.CartItem;
import com.java.excercise.model.entities.ProductImage;

import java.util.List;

public class CartMapper {

    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setPrice(cartItem.getProduct().getPrice());
        dto.setQuantity(cartItem.getQuantity());

        String imageUrl = null;
        List<ProductImage> images = cartItem.getProduct().getImages();
        if (images != null && !images.isEmpty()) {
            imageUrl = images.get(0).getUrl();
        }
        dto.setUrl(imageUrl);

        return dto;
    }
}
