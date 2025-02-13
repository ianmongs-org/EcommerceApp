package com.java.EcomerceApp.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Integer quantity;
    private Double productPrice;
    private Double discount;
    private Long productId;
    private String productName;
    private String productImage;
}