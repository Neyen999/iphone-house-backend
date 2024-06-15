package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.CategoryDto;
import com.personal.iphonehouse.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v${api.version}/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping("")
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto request) {
        return ResponseEntity.ok(categoryService.saveCategory(request));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> editCategory(@PathVariable Integer id, @RequestBody CategoryDto request) {
        return ResponseEntity.ok(categoryService.editCategory(id, request));
    }
}
