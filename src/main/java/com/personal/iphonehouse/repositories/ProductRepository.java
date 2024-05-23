package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByNameIgnoreCase(String name);
    List<Product> findAllAndIsDeleteFalse();
    @Query("SELECT pr FROM Product WHERE pr.category.name = :name")
    List<Product> findByDeletedFalseAndCategoryName(@Param("name") String name);
}