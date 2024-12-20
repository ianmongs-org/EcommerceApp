package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Cart;
import com.java.EcomerceApp.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.cart.cartId = ?1 AND c.product.productId = ?2")
    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    void deleteAllByCart(Cart cart);
}
