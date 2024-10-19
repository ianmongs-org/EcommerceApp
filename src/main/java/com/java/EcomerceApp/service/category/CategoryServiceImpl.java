package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.dto.CategoryDTO;
import com.java.EcomerceApp.dto.CategoryResponse;
import com.java.EcomerceApp.exception.APIException;
import com.java.EcomerceApp.exception.CategoryNotFoundException;
import com.java.EcomerceApp.model.Category;
import com.java.EcomerceApp.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        //map every category to a response using stream
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        //convert categoryDTO to category
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository.findByName(categoryDTO.getCategoryName()).ifPresent(existingCategory -> {
            throw new APIException("category with name " + categoryDTO.getCategoryName() +" already exists");
        });

        //save category
        Category savedCategory = categoryRepository.save(category);
        //convert saved category to categoryDTO
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        categoryRepository.delete(category);
        return "Category deleted successfully";
    }
    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        existingCategory.setName(categoryDTO.getCategoryName());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
}
