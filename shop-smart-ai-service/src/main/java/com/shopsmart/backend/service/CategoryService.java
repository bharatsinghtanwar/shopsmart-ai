package com.shopsmart.backend.service;

import com.shopsmart.backend.entity.Category;
import com.shopsmart.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category details) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(details.getName());
                    category.setDescription(details.getDescription());
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new RuntimeException("Category with ID " + id + " not found"));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
