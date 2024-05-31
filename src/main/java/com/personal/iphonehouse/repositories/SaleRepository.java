package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query("SELECT s FROM Sale s " +
            "WHERE (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND s.dateCreated > :startDate AND s.dateCreated < :endDate")
    List<Sale> findSalesBySearchAndDateBetween(@Param("search") String search,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate);
}
