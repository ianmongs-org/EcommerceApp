package com.java.EcomerceApp.service.category;

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
        categoryRepository.findByName(category.getName()).ifPresent(c -> {
            throw new IllegalStateException("Category already exists");
        });
        return categoryRepository.save(category);
    }

}
