package com.athletesedge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrLoggedEventDTO {
    private Long athleteId;
    private String exerciseName;
}