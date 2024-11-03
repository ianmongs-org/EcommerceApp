package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.CartDTO;

import com.java.EcomerceApp.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity) {
            CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
    @GetMapping("/carts")
    public ResponseEntity<CartDTO> getCart(){
        CartDTO cartDTO = cartService.getCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}
