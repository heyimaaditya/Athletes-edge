package com.athletesedge.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreatePrRequest {
    private Long athleteId;
    private String exerciseName;
    private double value;
    private String unit;
    private LocalDate recordDate;
}