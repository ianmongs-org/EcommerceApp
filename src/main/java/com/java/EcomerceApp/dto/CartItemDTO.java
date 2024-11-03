package com.java.EcomerceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private ProductDTO productDTO;
    private CartDTO cart;
    private Integer quantity;
    private Double productPrice;
    private Double discount;
}
