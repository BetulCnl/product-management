package com.betul.productmanagement.controller;

import com.betul.productmanagement.entity.Product;
import com.betul.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Pagination ve opsiyonel kategori filtresi ile ürün listeleme
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category) {

        Page<Product> products;
        if (category != null && !category.isEmpty()) {
            products = productService.findByCategory(category, page, size);
        } else {
            products = productService.getAllProducts(page, size);
        }
        return ResponseEntity.ok(products);
    }

    // ID ile ürün getir
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Ürün ekle
    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product saved = productService.saveProduct(product);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Ürün güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @Valid @RequestBody Product product) {
        return productService.getProductById(id)
                .map(existing -> {
                    product.setId(id);
                    Product updated = productService.saveProduct(product);
                    return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    // Ürün sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

