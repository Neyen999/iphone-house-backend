package com.personal.iphonehouse.runners;

import com.personal.iphonehouse.models.Category;
import com.personal.iphonehouse.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateBasicCategories implements CommandLineRunner {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        addBasicCategories();
    }

    public void addBasicCategories() {
        List<String> categories = List.of("Cargador", "Auricular", "Teléfono");

        for (String category : categories) {
            if (!categoryRepository.existsByNameIgnoreCase(category)) {
                categoryRepository.save(new Category(category));
            }
        }
    }
}
