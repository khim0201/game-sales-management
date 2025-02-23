package com.example.gamesales.repository;

import com.example.gamesales.entity.SalesStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SalesStatisticsRepository extends JpaRepository<SalesStatistics, Long> {
    List<SalesStatistics> findByStatisticsDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<SalesStatistics> findByStatisticsDateBetweenAndGameNo(
            LocalDate startDate, LocalDate endDate, Integer gameNo);
    
    @Query("SELECT SUM(s.totalSalesCount) FROM SalesStatistics s " +
           "WHERE s.statisticsDate BETWEEN :startDate AND :endDate")
    Integer getTotalSalesCount(@Param("startDate") LocalDate startDate, 
                             @Param("endDate") LocalDate endDate);
} 