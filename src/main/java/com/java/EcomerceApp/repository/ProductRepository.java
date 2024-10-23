package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
