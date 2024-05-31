package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Integer> {
    List<ProductSale> findByProductAndIsDeleteFalse(Product product);
}
