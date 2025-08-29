package com.athletesedge.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkoutTemplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // "Beginner Fat Loss - Week 1"
    private String goal; // "FAT_LOSS"
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private List<TemplateExercise> exercises;
}