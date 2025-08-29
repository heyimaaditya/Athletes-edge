package com.athletesedge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class GeneratedPlanDto {
  
    private Long athleteId;
    private LocalDate weekStartDate;
    
 
    @JsonProperty("weeklyPlan")
    private List<PlanDayDto> weeklyPlan;
}
