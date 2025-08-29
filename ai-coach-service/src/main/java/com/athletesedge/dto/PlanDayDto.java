package com.athletesedge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanDayDto {
    @JsonProperty("dayOfWeek")
    private int dayOfWeek;

    @JsonProperty("exerciseName")
    private String exerciseName;

    @JsonProperty("description")
    private String description;
}
