package com.athletesedge.dto;

import lombok.Data;

@Data
public class LogRecoveryRequest {
    private Long athleteId;
    private int sleepQuality;
    private int muscleSoreness;
    private int mood;
}
