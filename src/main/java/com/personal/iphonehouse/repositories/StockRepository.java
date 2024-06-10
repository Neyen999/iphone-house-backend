package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    @Query("SELECT s FROM Stock s " +
            "WHERE (:search IS NULL OR " +
            "LOWER(s.product.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.product.category.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "CAST(s.idealStock AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.initialStock AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.initialRegisterStock AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.initialCounterStock AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.registerReposition AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.counterReposition AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.registerSales AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.counterSales AS string) LIKE CONCAT('%', :search, '%') OR " +
            "CAST(s.finalStock AS string) LIKE CONCAT('%', :search, '%')) " +
            "AND s.dateCreated > :startDate AND s.dateCreated < :endDate "  + "AND s.isDelete = false")
    Page<Stock> searchStock(@Param("search") String search,
                            @Param("startDate") Date startDate,
                            @Param("endDate") Date endDate,
                            Pageable pageable);
    Stock findByDateCreatedAndProductAndIsDeleteFalse(Date date, Product product);
    List<Stock> findByProduct(Product product);
    boolean existsByProductIdAndDateCreatedAndIsDeleteFalse(Integer productId, Date dateCreated);
    @Query("SELECT s FROM Stock s WHERE s.dateCreated = :date AND s.isDelete = false")
    List<Stock> findByDateCreatedAndIsDeleteFalse(@Param("date") Date date);

    Optional<Stock> findTopByIsDeleteFalseOrderByDateCreatedDesc();
}
