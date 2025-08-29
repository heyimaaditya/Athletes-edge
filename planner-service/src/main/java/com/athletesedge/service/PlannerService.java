package com.athletesedge.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athletesedge.dto.ApplyTemplateRequest;
import com.athletesedge.dto.CreatePlanRequest;
import com.athletesedge.dto.ai.GeneratedPlanDto;
import com.athletesedge.model.PlannedWorkout;
import com.athletesedge.model.TemplateExercise;
import com.athletesedge.model.WorkoutTemplate;
import com.athletesedge.repository.PlannedWorkoutRepository;
import com.athletesedge.repository.WorkoutTemplateRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlannerService {
    @Autowired private PlannedWorkoutRepository planRepo;
    @Autowired private WorkoutTemplateRepository templateRepo;

    public PlannedWorkout createPlan(CreatePlanRequest req) {
        PlannedWorkout plan = PlannedWorkout.builder().athleteId(req.getAthleteId()).plannedDate(req.getPlannedDate()).exerciseName(req.getExerciseName()).description(req.getDescription()).build();
        return planRepo.save(plan);
    }

    public List<PlannedWorkout> getPlanForWeek(Long athleteId, LocalDate weekStartDate) {
        return planRepo.findByAthleteIdAndPlannedDateBetween(athleteId, weekStartDate, weekStartDate.plusDays(6));
    }

    public List<WorkoutTemplate> getAllTemplates() {
        return templateRepo.findAll();
    }
     public List<PlannedWorkout> getPlanForDay(Long athleteId, LocalDate date) {
        return planRepo.findByAthleteIdAndPlannedDate(athleteId, date);
    }
    public void applyTemplate(ApplyTemplateRequest req) {
        WorkoutTemplate template = templateRepo.findById(req.getTemplateId()).orElseThrow(() -> new RuntimeException("Template not found"));
        List<PlannedWorkout> newPlans = new ArrayList<>();
        
        for (TemplateExercise ex : template.getExercises()) {
            LocalDate exerciseDate = req.getWeekStartDate().plusDays(ex.getDayOfWeek() - 1);
            PlannedWorkout plan = PlannedWorkout.builder().athleteId(req.getAthleteId()).plannedDate(exerciseDate).exerciseName(ex.getExerciseName()).description(ex.getDescription()).build();
            newPlans.add(plan);
        }
        planRepo.saveAll(newPlans);
    }
    public void createBulkPlan(GeneratedPlanDto planDto) {
        List<PlannedWorkout> oldPlans = planRepo.findByAthleteIdAndPlannedDateBetween(
            planDto.getAthleteId(), 
            planDto.getWeekStartDate(), 
            planDto.getWeekStartDate().plusDays(6)
        );
        planRepo.deleteAll(oldPlans);
        List<PlannedWorkout> newPlans = planDto.getWeeklyPlan().stream()
            .map(day -> PlannedWorkout.builder()
                .athleteId(planDto.getAthleteId())
                .plannedDate(planDto.getWeekStartDate().plusDays(day.getDayOfWeek() - 1))
                .exerciseName(day.getExerciseName())
                .description(day.getDescription())
                .build())
            .collect(Collectors.toList());
        planRepo.saveAll(newPlans);
    }
}