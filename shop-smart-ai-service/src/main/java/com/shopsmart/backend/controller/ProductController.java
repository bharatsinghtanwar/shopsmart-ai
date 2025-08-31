package com.shopsmart.backend.controller;

import com.shopsmart.backend.dto.ApiResponse;
import com.shopsmart.backend.entity.Product;
import com.shopsmart.backend.service.ProductService;
import com.shopsmart.backend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        return ResponseUtil.success(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseUtil::success)
                .orElse(ResponseUtil.error("Product not found"));
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product,
                                        @RequestParam Long categoryId) {
        Product savedProduct = productService.saveProduct(product, categoryId);
        return ResponseEntity.ok(new ApiResponse<>(savedProduct, null));
    }


    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            return ResponseUtil.success(productService.updateProduct(id, productDetails, productDetails.getCategory().getId()));
        } catch (RuntimeException e) {
            return ResponseUtil.error("Product not found for update");
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseUtil.success("Product deleted successfully");
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(products, null));
    }
}
