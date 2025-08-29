package com.athletesedge.dto;
import com.athletesedge.model.GoalType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateGoalRequest {
    private Long athleteId;
    private String description;
    private GoalType type;
    private double targetValue;
    private LocalDate startDate;
    private LocalDate endDate;
}
