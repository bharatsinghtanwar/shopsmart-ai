package com.shopsmart.backend.controller;

import com.shopsmart.backend.dto.ApiResponse;
import com.shopsmart.backend.entity.Category;
import com.shopsmart.backend.service.CategoryService;
import com.shopsmart.backend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        return ResponseEntity.ok(ResponseUtil.success(categoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> ResponseEntity.ok(ResponseUtil.success(category)))
                .orElse(ResponseEntity.ok(ResponseUtil.error("Category not found")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> addCategory(@Valid @RequestBody Category category) {
        return ResponseEntity.ok(ResponseUtil.success(categoryService.saveCategory(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody Category details) {
        return ResponseEntity.ok(ResponseUtil.success(categoryService.updateCategory(id, details)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ResponseUtil.success(null));
    }
}
