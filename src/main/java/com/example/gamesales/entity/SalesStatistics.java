package com.example.gamesales.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "sales_statistics", 
       indexes = {
           @Index(name = "idx_date", columnList = "statistics_date"),
           @Index(name = "idx_game_no", columnList = "game_no")
       })
public class SalesStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "statistics_date", nullable = false)
    private LocalDate statisticsDate;

    @Column(name = "game_no")
    private Integer gameNo;

    @Column(name = "total_sales_count", nullable = false)
    private Integer totalSalesCount;

    @Column(name = "total_sales_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalSalesAmount;
} 