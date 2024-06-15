package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Product;
import com.personal.iphonehouse.models.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
            "AND s.dateCreated BETWEEN :startDate AND :endDate " +
            "AND s.isDelete = false")
    Page<Stock> searchStock(@Param("search") String search,
                            @Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate,
                            Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.dateCreated BETWEEN :startOfDay AND :endOfDay AND s.product = :product AND s.isDelete = false")
    Stock findByDateCreatedBetweenAndProductAndIsDeleteFalse(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay, @Param("product") Product product);

    List<Stock> findByProduct(Product product);
//    boolean existsByProductIdAndDateCreatedAndIsDeleteFalse(Integer productId, LocalDateTime dateCreated);
    @Query("SELECT COUNT(s) > 0 FROM Stock s WHERE s.product.id = :productId AND s.dateCreated BETWEEN :startOfDay AND :endOfDay AND s.isDelete = false")
    boolean existsByProductIdAndDateCreatedAndIsDeleteFalse(@Param("productId") Integer productId,
                                                            @Param("startOfDay") LocalDateTime startOfDay,
                                                            @Param("endOfDay") LocalDateTime endOfDay);
    @Query("SELECT s FROM Stock s WHERE s.dateCreated BETWEEN :startOfDay AND :endOfDay AND s.isDelete = false")
    List<Stock> findByDateCreatedAndIsDeleteFalse(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    Optional<Stock> findTopByIsDeleteFalseOrderByDateCreatedDesc();
}
