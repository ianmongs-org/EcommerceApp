package com.java.EcomerceApp.repository;


import com.java.EcomerceApp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
