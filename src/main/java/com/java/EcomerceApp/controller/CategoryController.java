package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.model.Category;
import com.java.EcomerceApp.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories(){

        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.CREATED);
    }
}
