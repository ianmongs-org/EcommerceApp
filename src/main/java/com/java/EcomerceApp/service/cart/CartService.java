package com.java.EcomerceApp.service.cart;

import com.java.EcomerceApp.dto.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}
