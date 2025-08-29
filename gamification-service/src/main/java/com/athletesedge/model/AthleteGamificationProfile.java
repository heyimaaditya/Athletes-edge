package com.athletesedge.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gamification_profiles")
public class AthleteGamificationProfile {
    @Id
    private Long athleteId;

    private String avatarType;
    private int level;
    private int currentXp;
    private int nextLevelXp;
    private int skillPoints;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "athlete_skills", joinColumns = @JoinColumn(name = "athlete_id"))
    @MapKeyColumn(name = "skill_name")
    @Column(name = "skill_level")
    private Map<String, Integer> skills = new HashMap<>();
}