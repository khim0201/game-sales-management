package com.example.gamesales.controller;

import com.example.gamesales.entity.GameSale;
import com.example.gamesales.service.GameSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GameSaleController {
    private GameSaleService gameSaleService;

    @Autowired
    public GameSaleController(GameSaleService gameSaleService) {
        this.gameSaleService = gameSaleService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        gameSaleService.importCsvFile(file);
        return ResponseEntity.ok("File import started");
    }

    @GetMapping("/getGameSales")
    public ResponseEntity<Page<GameSale>> getGameSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        Page<GameSale> result;
        if (startDate != null && endDate != null) {
            result = gameSaleService.getGameSalesByDateRange(startDate, endDate, page, size);
        } else if (minPrice != null && maxPrice != null) {
            result = gameSaleService.getGameSalesByPriceRange(minPrice, maxPrice, page, size);
        } else {
            result = gameSaleService.getGameSales(page, size);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getTotalSales")
    public ResponseEntity<Map<String, Object>> getTotalSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Integer gameNo) {

        Map<String, Object> result;
        if (gameNo != null) {
            result = gameSaleService.getTotalSalesByGameNo(startDate, endDate, gameNo);
        } else {
            result = gameSaleService.getTotalSales(startDate, endDate);
        }
        return ResponseEntity.ok(result);
    }
} 