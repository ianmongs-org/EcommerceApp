package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.config.AppConstants;
import com.java.EcomerceApp.dto.ProductDTO;
import com.java.EcomerceApp.dto.ProductResponse;
import com.java.EcomerceApp.model.Product;
import com.java.EcomerceApp.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/category/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.addProduct(productDTO, categoryId),HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                          @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId), HttpStatus.FOUND);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword) {
        return new ResponseEntity<>(productService.getProductsByKeyword(keyword), HttpStatus.FOUND);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductDTO productDTO) {
        return new ResponseEntity<>(productService.updateProduct(productId, productDTO), HttpStatus.OK);
    }

    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String result = productService.deleteProduct(productId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId, file), HttpStatus.OK);
    }
}
