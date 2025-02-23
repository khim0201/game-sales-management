package com.example.gamesales.repository;

import com.example.gamesales.entity.ImportProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportProgressRepository extends JpaRepository<ImportProgress, Long> {
    ImportProgress findTopByOrderByStartTimeDesc();
} 