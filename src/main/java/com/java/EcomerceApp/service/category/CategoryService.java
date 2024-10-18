package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.dto.CategoryDTO;
import com.java.EcomerceApp.dto.CategoryResponse;
import com.java.EcomerceApp.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
     CategoryResponse getAllCategories();
     CategoryDTO addCategory(CategoryDTO categoryDTO);
     Category updateCategory(Long categoryId, Category updatedCategory);
     String deleteCategory(Long categoryId);
}
