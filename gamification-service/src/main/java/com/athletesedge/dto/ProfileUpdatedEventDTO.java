package com.athletesedge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdatedEventDTO {
    private Long athleteId;
    private int newLevel;
    private int newXp;
    private int nextLevelXp;
    private boolean leveledUp;
}
