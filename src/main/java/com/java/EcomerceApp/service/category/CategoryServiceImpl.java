package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.exception.CategoryExistsException;
import com.java.EcomerceApp.exception.CategoryNotFoundException;
import com.java.EcomerceApp.model.Category;
import com.java.EcomerceApp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        categoryRepository.findByName(category.getName()).ifPresent(existingCategory -> {
            throw new CategoryExistsException("category already exists");
        });
        return categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        categoryRepository.delete(category);
        return "Category deleted successfully";
    }
    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        return categoryRepository.save(existingCategory);
    }
}
