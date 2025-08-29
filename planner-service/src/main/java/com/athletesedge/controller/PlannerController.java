package com.athletesedge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.athletesedge.dto.ApplyTemplateRequest;
import com.athletesedge.dto.CreatePlanRequest;
import com.athletesedge.dto.ai.GeneratedPlanDto;
import com.athletesedge.model.PlannedWorkout;
import com.athletesedge.model.WorkoutTemplate;
import com.athletesedge.service.PlannerService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {
    @Autowired private PlannerService service;

    @GetMapping("/templates")
    public ResponseEntity<List<WorkoutTemplate>> getAllTemplates() {
        return ResponseEntity.ok(service.getAllTemplates());
    }

    @GetMapping("/plan/athlete/{athleteId}")
    public ResponseEntity<List<PlannedWorkout>> getPlan(@PathVariable Long athleteId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate) {
        return ResponseEntity.ok(service.getPlanForWeek(athleteId, weekStartDate));
    }

    @PostMapping("/plan")
    public ResponseEntity<PlannedWorkout> createPlan(@RequestBody CreatePlanRequest request) {
        return new ResponseEntity<>(service.createPlan(request), HttpStatus.CREATED);
    }
    
    @PostMapping("/plan/apply-template")
    public ResponseEntity<Void> applyTemplate(@RequestBody ApplyTemplateRequest request) {
        service.applyTemplate(request);
        return ResponseEntity.ok().build();
    }

     @GetMapping("/plan/athlete/{athleteId}/date/{date}")
    public ResponseEntity<List<PlannedWorkout>> getPlanForSingleDay(
        @PathVariable Long athleteId, 
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getPlanForDay(athleteId, date));
    }
    @PostMapping("/plan/bulk")
    public ResponseEntity<Void> createBulkPlan(@RequestBody GeneratedPlanDto plan) {
        service.createBulkPlan(plan);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}