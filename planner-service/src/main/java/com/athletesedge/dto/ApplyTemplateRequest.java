package com.athletesedge.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplyTemplateRequest {
    private Long athleteId;
    private Long templateId;
    private LocalDate weekStartDate; // Monday of the week to apply the plan
}
