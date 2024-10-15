package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
     List<Category> getAllCategories();
//     Optional<Category> getCategoryById(Long categoryId);
     Category addCategory(Category category);
//     Category updateCategory(Category category);
//     void deleteCategory(Long categoryId);
}
