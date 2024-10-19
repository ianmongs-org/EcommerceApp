package com.java.EcomerceApp.service.category;

import com.java.EcomerceApp.dto.CategoryDTO;
import com.java.EcomerceApp.dto.CategoryResponse;
import com.java.EcomerceApp.exception.APIException;
import com.java.EcomerceApp.exception.CategoryNotFoundException;
import com.java.EcomerceApp.model.Category;
import com.java.EcomerceApp.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
             ? Sort.by(sortBy).ascending() 
             : Sort.by(sortBy).descending();
        //represents the request of the specific page
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page <Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        //map every category to a response using stream
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTOS);
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        //convert categoryDTO to category
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository.findByName(category.getName()).ifPresent(existingCategory -> {
            throw new APIException("category with name " + category.getName() +" already exists");
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
