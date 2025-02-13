package com.java.EcomerceApp.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String image;
    private String productDescription;
    private Integer quantity;
    private Double price;
    private Double specialPrice;
    private Double discount;
}
