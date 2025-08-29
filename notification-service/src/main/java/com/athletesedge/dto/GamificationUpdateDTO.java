package com.athletesedge.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class GamificationUpdateDTO {
    private Long athleteId;
    private int level;
    private int currentXp;
    private int nextLevelXp;
    private int skillPoints;
    private boolean leveledUp;
}

