package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.dto.CategoryDTO;
import com.java.EcomerceApp.dto.CategoryResponse;


public interface CategoryService {
     CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
     CategoryDTO addCategory(CategoryDTO categoryDTO);
     CategoryDTO updateCategory(Long categoryId, CategoryDTO updatedCategory);
     String deleteCategory(Long categoryId);
}
