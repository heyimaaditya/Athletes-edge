package com.athletesedge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCompletedEventDTO {
    private Long athleteId;
    private String description;
}
