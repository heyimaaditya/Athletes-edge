package com.athletesedge.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlanDayDto {
    @JsonProperty("dayOfWeek")
    private int dayOfWeek;

    @JsonProperty("exerciseName")
    private String exerciseName;

    @JsonProperty("description")
    private String description;
}
