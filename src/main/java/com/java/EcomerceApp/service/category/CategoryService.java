package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
     List<Category> getAllCategories();
     Category addCategory(Category category);
     Category updateCategory(Long categoryId, Category updatedCategory);
     String deleteCategory(Long categoryId);
}
