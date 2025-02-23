package com.example.gamesales.service;

import com.example.gamesales.entity.GameSale;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface GameSaleService {
    void importCsvFile(MultipartFile file) throws IOException;
    
    Page<GameSale> getGameSales(int page, int size);
    
    Page<GameSale> getGameSalesByDateRange(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            int page, 
            int size);
    
    Page<GameSale> getGameSalesByPriceRange(
            BigDecimal minPrice, 
            BigDecimal maxPrice, 
            int page, 
            int size);
    
    Map<String, Object> getTotalSales(LocalDateTime startDate, LocalDateTime endDate);
    
    Map<String, Object> getTotalSalesByGameNo(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Integer gameNo);
} 