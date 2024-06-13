package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.CategoryDto;
import com.personal.iphonehouse.models.Category;
import com.personal.iphonehouse.repositories.CategoryRepository;
import com.personal.iphonehouse.utils.DateUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public CategoryDto saveCategory(CategoryDto request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName()))
            throw new RuntimeException("Nombre ya existe");

        Category category = new Category();
        category.setName(request.getName());


        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto editCategory(Integer id, CategoryDto request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName()))
            throw new RuntimeException("Nombre ya existe");

        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));
        category.setName(request.getName());
        category.setDateUpdated(LocalDateTime.now());

        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }
}
