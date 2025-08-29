package com.athletesedge.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanningContext {
    private AthleteProfileDto profile;
    private AthleteStatsDto stats;
}