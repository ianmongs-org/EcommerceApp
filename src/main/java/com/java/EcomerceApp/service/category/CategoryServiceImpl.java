package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.exception.APIException;
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
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        return categories;
    }

    @Override
    public Category addCategory(Category category) {
        categoryRepository.findByName(category.getName()).ifPresent(existingCategory -> {
            throw new APIException("category with name " + category.getName() +" already exists");
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
