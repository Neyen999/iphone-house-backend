package com.personal.iphonehouse.repositories;

import com.personal.iphonehouse.models.PruebaFecha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PruebaFechaRepository extends JpaRepository<PruebaFecha, Long> {
    List<PruebaFecha> findByDateCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);
}
