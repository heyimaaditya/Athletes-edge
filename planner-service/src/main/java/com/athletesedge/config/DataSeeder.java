package com.athletesedge.config;

import com.athletesedge.model.*;
import com.athletesedge.repository.WorkoutTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {
    private final WorkoutTemplateRepository templateRepository;
    public DataSeeder(WorkoutTemplateRepository repo) { this.templateRepository = repo; }

    @Override
    public void run(String... args) throws Exception {
        if (templateRepository.count() == 0) { 
            TemplateExercise day1 = TemplateExercise.builder().dayOfWeek(1).exerciseName("Full Body Strength A").description("Squat 3x5, Bench Press 3x5, Rows 3x5").build();
            TemplateExercise day3 = TemplateExercise.builder().dayOfWeek(3).exerciseName("Full Body Strength B").description("Deadlift 1x5, Overhead Press 3x5, Pull-ups 3xAMRAP").build();
            TemplateExercise day5 = TemplateExercise.builder().dayOfWeek(5).exerciseName("Full Body Strength A").description("Squat 3x5, Bench Press 3x5, Rows 3x5").build();

            WorkoutTemplate strengthTemplate = WorkoutTemplate.builder()
                .name("Beginner Strength Program")
                .goal("MUSCLE_GAIN")
                .description("A 3-day full body routine for beginners.")
                .exercises(Arrays.asList(day1, day3, day5))
                .build();
            
            templateRepository.save(strengthTemplate);
            System.out.println("Sample workout templates seeded.");
        }
    }
}