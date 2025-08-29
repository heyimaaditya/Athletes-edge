package com.athletesedge.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlannedWorkout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long athleteId;
    private LocalDate plannedDate;
    private String exerciseName;
    private String description; 
}