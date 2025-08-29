package com.athletesedge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "athlete_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class AthleteStats {

    @Id
    private Long athleteId; 

    private double totalTrainingLoad;
    private int workoutCount;
    private LocalDate lastWorkoutDate;
}