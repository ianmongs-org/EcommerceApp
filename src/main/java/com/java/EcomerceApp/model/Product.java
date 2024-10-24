package com.java.EcomerceApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    private String productName;

    @NotBlank(message = "Product description is required")
    @Size(max = 500, message = "Product description cannot exceed 500 characters")
    private String productDescription;

    @NotBlank(message = "Product image URL is required")
    //@Pattern(regexp = "^(http|https)://.*", message = "Image must be a valid URL")
    private String image;

    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than 0")
    private Double discount;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be less than 0")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    private Double specialPrice;

    // relationships
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
