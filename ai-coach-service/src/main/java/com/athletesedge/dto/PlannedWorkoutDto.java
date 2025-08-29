package com.athletesedge.dto;

import lombok.Data;
import java.time.LocalDate; 


@Data
public class PlannedWorkoutDto {
    private String exerciseName;
    private String description;
    

    private LocalDate plannedDate; 
}
