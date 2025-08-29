package com.athletesedge.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TemplateExercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dayOfWeek; // 1=Monday, 2=Tuesday, etc.
    private String exerciseName;
    private String description;
}
