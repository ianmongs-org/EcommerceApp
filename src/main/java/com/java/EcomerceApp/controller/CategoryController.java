package com.java.EcomerceApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.EcomerceApp.config.AppConstants;
import com.java.EcomerceApp.dto.CategoryDTO;
import com.java.EcomerceApp.dto.CategoryResponse;
import com.java.EcomerceApp.service.category.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all categories", description = "Retrieve all categories with pagination and sorting",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))
            )
    )
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                             @RequestParam(name ="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize,sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new category", description = "Create a new category",
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Category created",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))
            )
    )
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.addCategory(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    @Operation(summary = "Update a category", description = "Update an existing category by ID",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Category updated",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))
            )
    )
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO category){
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, category), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    @Operation(summary = "Delete a category", description = "Delete a category by ID",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Category deleted",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    )
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
}
