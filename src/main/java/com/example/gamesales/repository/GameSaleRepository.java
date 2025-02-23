package com.example.gamesales.repository;

import com.example.gamesales.entity.GameSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface GameSaleRepository extends JpaRepository<GameSale, Long> {
    Page<GameSale> findByDateOfSaleBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Page<GameSale> findBySalePriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("SELECT g FROM GameSale g WHERE g.dateOfSale BETWEEN :startDate AND :endDate " +
           "AND g.salePrice BETWEEN :minPrice AND :maxPrice")
    Page<GameSale> findByDateAndPriceRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
} 