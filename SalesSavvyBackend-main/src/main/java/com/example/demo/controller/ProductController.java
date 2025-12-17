package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(required = false) String category,
            HttpServletRequest request) {
        try {
            // Retrieve authenticated user from the request attribute set by the filter
            User authenticatedUser = (User) request.getAttribute("authenticatedUser");
            
            // Fetch products based on the category filter
            List<Product> products = productService.getProductsByCategory(category);

            // Build the response
            Map<String, Object> response = new HashMap<>();
            
            // Add user info if authenticated
            if (authenticatedUser != null) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("name", authenticatedUser.getUsername());
                userInfo.put("role", authenticatedUser.getRole().name());
                response.put("user", userInfo);
            }

            // Add product details
            List<Map<String, Object>> productList = new ArrayList<>();
            for (Product product : products) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("productId", product.getProductId());
                productDetails.put("product_id", product.getProductId());
                productDetails.put("name", product.getName());
                productDetails.put("description", product.getDescription());
                productDetails.put("price", product.getPrice());
                productDetails.put("stock", product.getStock());

                // Fetch product images
                List<String> images = productService.getProductImages(product.getProductId());
                productDetails.put("images", images);
                if (images != null && !images.isEmpty()) {
                    productDetails.put("imageUrl", images.get(0));
                }

                productList.add(productDetails);
            }
            response.put("products", productList);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Public endpoint for landing page (no authentication required)
    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> getPublicProducts(
            @RequestParam(required = false) String category) {
        try {
            // Fetch products based on the category filter
            List<Product> products = productService.getProductsByCategory(category);

            // Build the response
            Map<String, Object> response = new HashMap<>();

            // Add product details
            List<Map<String, Object>> productList = new ArrayList<>();
            for (Product product : products) {
                Map<String, Object> productDetails = new HashMap<>();
                productDetails.put("productId", product.getProductId());
                productDetails.put("product_id", product.getProductId());
                productDetails.put("name", product.getName());
                productDetails.put("description", product.getDescription());
                productDetails.put("price", product.getPrice());
                productDetails.put("stock", product.getStock());

                // Fetch product images
                List<String> images = productService.getProductImages(product.getProductId());
                productDetails.put("images", images);
                if (images != null && !images.isEmpty()) {
                    productDetails.put("imageUrl", images.get(0));
                }

                productList.add(productDetails);
            }
            response.put("products", productList);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
