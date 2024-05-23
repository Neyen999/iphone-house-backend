package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Category;
import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findByProduct(Product product);
    Stock findByProductAndDateCreatedAndIsDeleteFalse(Product product, Date dateCreated);
    Page<Stock> findAllByProductAndIsDeleteFalseAndDateCreatedBetween(Product product, Date startDate, Date endDate, Pageable pageable);
    Page<Stock> findAllByProductCategoryIdAndIsDeleteFalse(Integer categoryId, Pageable pageable);
    Page<Stock> findAllByProductCategoryIdAndIsDeleteFalseAndDateCreatedBetween(Integer categoryId, Date startDate, Date endDate, Pageable pageable);
    boolean existsByProductIdAndDateCreatedAndIsDeleteFalse(Integer productId, Date dateCreated);
    List<Stock> findByDateCreatedAndIsDeleteFalse(Date date);

}
