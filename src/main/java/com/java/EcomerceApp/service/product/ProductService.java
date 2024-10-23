package com.java.EcomerceApp.service.product;

import com.java.EcomerceApp.dto.ProductDTO;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
}
