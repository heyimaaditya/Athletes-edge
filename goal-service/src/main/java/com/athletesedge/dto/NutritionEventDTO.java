package com.athletesedge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionEventDTO {
    private Long athleteId;
    private Integer calories;
    private Double proteinGrams;
    private Double carbsGrams;
    private Double fatGrams;
    private LocalDate logDate;
}
