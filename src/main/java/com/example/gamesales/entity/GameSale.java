package com.example.gamesales.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "game_sales")
public class GameSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_no", nullable = false)
    private Integer gameNo;

    @Column(name = "game_name", length = 20, nullable = false)
    private String gameName;

    @Column(name = "game_code", length = 5, nullable = false)
    private String gameCode;

    @Column(nullable = false)
    private Integer type;

    @Column(name = "cost_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal costPrice;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal tax;

    @Column(name = "sale_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal salePrice;

    @Column(name = "date_of_sale", nullable = false)
    private LocalDateTime dateOfSale;
} 