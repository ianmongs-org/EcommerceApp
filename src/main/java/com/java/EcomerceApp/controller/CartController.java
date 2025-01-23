package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.CartDTO;
import com.java.EcomerceApp.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Cart", description = "Cart management APIs")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/cart/products/{productId}/quantity/{quantity}")
    @Operation(summary = "Add product to cart", description = "Add a product to the cart with the specified quantity")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/carts")
    @Operation(summary = "Get cart", description = "Retrieve the current cart")
    public ResponseEntity<CartDTO> getCart() {
        CartDTO cartDTO = cartService.getCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}