package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.CategoryDTO;
import com.java.EcomerceApp.dto.CategoryResponse;
import com.java.EcomerceApp.exception.APIException;
import com.java.EcomerceApp.model.Category;
import com.java.EcomerceApp.service.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                             @RequestParam(name ="pageSize",defaultValue = "10") Integer pageSize){
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.addCategory(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO category){
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, category), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
}
