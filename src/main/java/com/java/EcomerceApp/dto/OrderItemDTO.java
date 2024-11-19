package com.java.EcomerceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Integer quantity;
    private Double productPrice;
    private Double discount;
    private ProductDTO product;
}
