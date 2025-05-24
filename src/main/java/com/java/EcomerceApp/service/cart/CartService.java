package com.java.EcomerceApp.service.cart;

import com.java.EcomerceApp.dto.CartDTO;
import com.java.EcomerceApp.model.Cart;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    CartDTO getCart();

    CartDTO removeProductFromCart(Long productId);

    CartDTO updateProductQuantity(Long productId, Integer quantity);

    void clearCart();
}
