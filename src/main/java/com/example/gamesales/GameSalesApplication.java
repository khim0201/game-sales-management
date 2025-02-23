package com.example.gamesales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GameSalesApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameSalesApplication.class, args);
        System.out.println("Successfully Launch");
    }
} 