package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.ProductDto;
import com.personal.iphonehouse.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v${api.version}/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(value = "category", required = false) String category) {
        return ResponseEntity.ok(productService.getAllProducts(category));
    }

    @PostMapping("")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto request) {
        return ResponseEntity.ok(productService.saveProduct(request));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> editProduct(@RequestBody ProductDto request, @PathVariable Integer id) {
        return ResponseEntity.ok(productService.editProduct(request, id));
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }
}
