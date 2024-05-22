package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByNameIgnoreCase(String name);
}
