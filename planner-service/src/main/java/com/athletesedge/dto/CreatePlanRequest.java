package com.athletesedge.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreatePlanRequest {
    private Long athleteId;
    private LocalDate plannedDate;
    private String exerciseName;
    private String description;
}
