package com.example.gamesales.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "import_progress")
public class ImportProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "total_records")
    private Integer totalRecords;

    @Column(name = "processed_records")
    private Integer processedRecords;

    @Column(name = "status", nullable = false)
    private String status;  // PENDING, PROCESSING, COMPLETED, FAILED

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "error_message")
    private String errorMessage;
} 