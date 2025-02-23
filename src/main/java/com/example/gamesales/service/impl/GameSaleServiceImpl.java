package com.example.gamesales.service.impl;

import com.example.gamesales.entity.GameSale;
import com.example.gamesales.entity.ImportProgress;
import com.example.gamesales.entity.SalesStatistics;
import com.example.gamesales.repository.GameSaleRepository;
import com.example.gamesales.repository.ImportProgressRepository;
import com.example.gamesales.repository.SalesStatisticsRepository;
import com.example.gamesales.service.GameSaleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class GameSaleServiceImpl implements GameSaleService {
    private final GameSaleRepository gameSaleRepository;
    private final ImportProgressRepository importProgressRepository;
    private final SalesStatisticsRepository salesStatisticsRepository;
    private final EntityManagerFactory entityManagerFactory;
    private static final BigDecimal TAX_RATE = new BigDecimal("0.09");
    private static final int BATCH_SIZE = 1000; // to process it by batches
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public GameSaleServiceImpl(GameSaleRepository gameSaleRepository, 
                             ImportProgressRepository importProgressRepository,
                             SalesStatisticsRepository salesStatisticsRepository,
                             EntityManagerFactory entityManagerFactory) {
        this.gameSaleRepository = gameSaleRepository;
        this.importProgressRepository = importProgressRepository;
        this.salesStatisticsRepository = salesStatisticsRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void importCsvFile(MultipartFile file) throws IOException {
        ImportProgress progress = new ImportProgress();
        progress.setFileName(file.getOriginalFilename());
        progress.setStatus("PROCESSING");
        progress.setStartTime(LocalDateTime.now());
        progress.setProcessedRecords(0);
        importProgressRepository.save(progress);

        System.out.println("Start Importing: " + file.getOriginalFilename());
        System.out.println("File Size: " + file.getSize() / 1024 + " KB");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); //Skip header
            
            List<GameSale> batch = new ArrayList<>();
            String line;
            int totalRecords = 0;
            long startTime = System.currentTimeMillis();
            
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 9) continue; //total 9 fields
                
                GameSale sale = parseGameSale(fields);
                if (sale != null) {
                    batch.add(sale);
                    totalRecords++;
                }
                
                if (batch.size() >= BATCH_SIZE) {
                    saveBatch(batch);
                    updateProgress(progress, totalRecords, startTime);
                    batch.clear();
                }
            }
            
            // To process last batch
            if (!batch.isEmpty()) {
                saveBatch(batch);
                updateProgress(progress, totalRecords, startTime);
            }
            
            // Update Progress
            progress.setStatus("COMPLETED");
            progress.setTotalRecords(totalRecords);
            progress.setProcessedRecords(totalRecords);
            progress.setEndTime(LocalDateTime.now());
            importProgressRepository.save(progress);
            
            System.out.printf("Imported Done! Total Record: %d, Total Duration: %.2f s%n",
                    totalRecords,
                    (System.currentTimeMillis() - startTime) / 1000.0);
            
        } catch (Exception e) {
            handleImportError(progress, e);
            throw new RuntimeException("Imported Failed", e);
        }
    }

    //Set all the required fields
    private GameSale parseGameSale(String[] fields) {
        try {
            GameSale sale = new GameSale();
            sale.setGameNo(Integer.parseInt(fields[1].trim()));
            sale.setGameName(fields[2].trim());
            sale.setGameCode(fields[3].trim());
            sale.setType(Integer.parseInt(fields[4].trim()));
            
            BigDecimal costPrice = new BigDecimal(fields[5].trim());
            BigDecimal tax = costPrice.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
            BigDecimal salePrice = costPrice.add(tax);
            
            sale.setCostPrice(costPrice);
            sale.setTax(tax);
            sale.setSalePrice(salePrice);
            sale.setDateOfSale(LocalDateTime.parse(fields[8].trim(), DATE_FORMATTER));
            
            return sale;
        } catch (Exception e) {
            System.err.println("Fail Analysis: " + String.join(",", fields));
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    protected void saveBatch(List<GameSale> batch) {
        gameSaleRepository.saveAll(batch);
    }

    private void updateProgress(ImportProgress progress, int totalRecords, long startTime) {
        long currentTime = System.currentTimeMillis();
        double elapsedSeconds = (currentTime - startTime) / 1000.0;
        double recordsPerSecond = totalRecords / elapsedSeconds;
        
        System.out.printf("Had processed %d records, Total Time: %.2f s, Speed: %.2f%n",
                totalRecords, elapsedSeconds, recordsPerSecond);
        
        progress.setProcessedRecords(totalRecords);
        importProgressRepository.save(progress);
    }

    private void handleImportError(ImportProgress progress, Exception e) {
        System.err.println("Imported Fail: " + e.getMessage());
        e.printStackTrace();
        progress.setStatus("FAILED");
        progress.setErrorMessage(e.getMessage());
        progress.setEndTime(LocalDateTime.now());
        importProgressRepository.save(progress);
    }

    @Override
    public Page<GameSale> getGameSales(int page, int size) {
        return gameSaleRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<GameSale> getGameSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        return gameSaleRepository.findByDateOfSaleBetween(startDate, endDate, PageRequest.of(page, size));
    }

    @Override
    public Page<GameSale> getGameSalesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        return gameSaleRepository.findBySalePriceBetween(minPrice, maxPrice, PageRequest.of(page, size));
    }

    @Override
    public Map<String, Object> getTotalSales(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        List<SalesStatistics> statistics = salesStatisticsRepository.findByStatisticsDateBetween(start, end);
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", statistics.stream()
                .mapToInt(SalesStatistics::getTotalSalesCount)
                .sum());
        result.put("totalAmount", statistics.stream()
                .map(SalesStatistics::getTotalSalesAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return result;
    }

    @Override
    public Map<String, Object> getTotalSalesByGameNo(LocalDateTime startDate, LocalDateTime endDate, Integer gameNo) {
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        List<SalesStatistics> statistics = salesStatisticsRepository
                .findByStatisticsDateBetweenAndGameNo(start, end, gameNo);
        
        Map<String, Object> result = new HashMap<>();
        result.put("gameNo", gameNo);
        result.put("totalCount", statistics.stream()
                .mapToInt(SalesStatistics::getTotalSalesCount)
                .sum());
        result.put("totalAmount", statistics.stream()
                .map(SalesStatistics::getTotalSalesAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return result;
    }
} 