package com.personal.iphonehouse.services;

import com.personal.iphonehouse.dtos.CategoryDto;
import com.personal.iphonehouse.models.Category;
import com.personal.iphonehouse.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public CategoryDto saveCategory(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name))
            throw new RuntimeException("Nombre ya existe");

        Category category = new Category();
        category.setName(name);


        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
}
