package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByNameIgnoreCase(String name);
    List<Product> findByIsDeleteFalse();
    @Query("SELECT pr FROM Product pr " +
            "WHERE (:search IS NULL OR " +
            "LOWER(pr.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(pr.category.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND pr.isDelete = false")
    List<Product> findBySearchAndIsDeleteFalseAndDateBetween(@Param("search") String search);
    Optional<Product> findByNameEqualsIgnoreCase(String name);
}
