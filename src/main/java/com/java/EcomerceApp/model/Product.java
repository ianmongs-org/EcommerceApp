package com.java.EcomerceApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String productDescription;
    private Integer quantity;
    private Double price;
    private Double specialPrice;

    //relationships
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
