package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Sale;
import jakarta.persistence.TemporalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
//    @Query("SELECT s FROM Sale s " +
//            "WHERE (:search IS NULL OR LOWER(s.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
//            "OR :search IS NULL OR CAST(s.userPhoneNumber AS string) LIKE CONCAT('%', :search, '%')) " +
//            "AND (:startDate IS NULL OR :endDate IS NULL OR (s.dateCreated > :startDate AND s.dateCreated < :endDate)) " +
//            "AND s.testerSale = false " +
//            "ORDER BY s.dateCreated DESC")
//    Page<Sale> findSalesBySearchAndDateBetweenAndTesterSaleFalse(@Param("search") String search,
//                                                                 @Param("startDate") Date startDate,
//                                                                 @Param("endDate") Date endDate,
//                                                                 Pageable pageable);

//    @Query("SELECT s FROM Sale s " +
//            "WHERE (:search IS NULL OR LOWER(s.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
//            "OR :search IS NULL OR CAST(s.userPhoneNumber AS string) LIKE CONCAT('%', :search, '%')) " +
//            "AND (:startDate IS NULL OR s.dateCreated = :startDate) " +
////            "AND (:startDate IS NULL OR DAY(s.dateCreated) = DAY(:startDate) AND MONTH(s.dateCreated) = MONTH(:startDate) AND YEAR(s.dateCreated) = YEAR(:startDate) ) " +
//            "AND s.testerSale = false " +
//            "ORDER BY s.dateCreated DESC")
//    Page<Sale> findSalesBySearchAndDateBetweenAndTesterSaleFalse(@Param("search") String search,
//                                                                 @Param("startDate") Date startDate,
//                                                                 Pageable pageable);
    @Query("SELECT s FROM Sale s " +
            "WHERE (:search IS NULL OR LOWER(s.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR :search IS NULL OR CAST(s.userPhoneNumber AS string) LIKE CONCAT('%', :search, '%')) " +
            "AND ( " +
            "(:startDate IS NULL AND :endDate IS NULL) " +
            "OR (:startDate IS NOT NULL AND :endDate IS NULL AND s.dateCreated = :startDate) " +
            "OR (:startDate IS NULL AND :endDate IS NOT NULL AND s.dateCreated = :endDate) " +
            "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND s.dateCreated BETWEEN :startDate AND :endDate) " +
            ") " +
            "AND s.testerSale = false " +
            "ORDER BY s.dateCreated DESC")
    Page<Sale> findSalesBySearchAndDateBetweenAndTesterSaleFalse(@Param("search") String search,
                                                                 @Param("startDate") Date startDate,
                                                                 @Param("endDate") Date endDate,
                                                                 Pageable pageable);

}
