package com.athletesedge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutEventDTO {
    private Long athleteId;
    private String workoutType;
    private Integer durationMinutes;
    private Double intensity;
    private LocalDate workoutDate;
    private Double trainingLoad;
}