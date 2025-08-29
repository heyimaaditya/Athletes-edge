package com.athletesedge.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class GeneratedPlanDto {
    private Long athleteId;
    private LocalDate weekStartDate;

    @JsonProperty("weeklyPlan")
    private List<PlanDayDto> weeklyPlan;
}