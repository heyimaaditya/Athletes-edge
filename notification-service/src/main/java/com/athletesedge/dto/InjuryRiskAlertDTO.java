
package com.athletesedge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InjuryRiskAlertDTO {
    private Long athleteId;
    private double acwr;
    private String message;
    private String riskLevel;
}