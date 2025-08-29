package com.athletesedge.dto;
import lombok.Builder;
import lombok.Data;
@Data @Builder public class DailyContext { private ReadinessDto readiness; private PlannedWorkoutDto plan; private AcwrDto acwr; }