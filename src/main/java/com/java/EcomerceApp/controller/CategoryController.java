package com.java.EcomerceApp.controller;

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
    public ResponseEntity<List<Category>> getAllCategories(){

        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category){
        try {
            return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.CREATED);
        } catch (APIException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category category){
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, category), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
}
