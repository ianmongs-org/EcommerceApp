package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Category;
import com.java.EcomerceApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductName(String productName);

    List<Product> findByCategoryOrderByPriceAsc(Category category);
}
