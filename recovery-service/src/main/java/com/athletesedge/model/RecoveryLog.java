package com.athletesedge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "recovery_logs", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"athleteId", "logDate"}) 
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoveryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long athleteId;
    private LocalDate logDate;

    private int sleepQuality;    // 1-5
    private int muscleSoreness;  // 1-5 (1=best, 5=worst)
    private int mood;            // 1-5

    private int readinessScore;  // 0-100
}