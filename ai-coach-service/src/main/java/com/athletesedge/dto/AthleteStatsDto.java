package com.athletesedge.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AthleteStatsDto {
    private double totalTrainingLoad = 0; 
    private int workoutCount = 0;         
}
