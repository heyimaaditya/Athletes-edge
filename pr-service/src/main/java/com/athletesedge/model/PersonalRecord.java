package com.athletesedge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "personal_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long athleteId;
    private String exerciseName; // "5k Run", "Deadlift", "Bench Press"
    private double value; // 1200 (for seconds), 150 (for kg)
    private String unit; // "seconds", "kg", "km"
    private LocalDate recordDate;
}