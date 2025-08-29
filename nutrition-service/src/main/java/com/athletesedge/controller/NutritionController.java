package com.athletesedge.controller;

import com.athletesedge.model.NutritionLog;
import com.athletesedge.repository.NutritionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.athletesedge.service.NutritionService; 
import java.util.List;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    @Autowired
    private NutritionLogRepository nutritionLogRepository;

     @Autowired
    private NutritionService nutritionService; 
    @PostMapping
    public ResponseEntity<NutritionLog> createNutritionLog(@RequestBody NutritionLog nutritionLog) {
      
        NutritionLog savedLog = nutritionService.createLog(nutritionLog);
        return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
    }

  
    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<NutritionLog>> getLogsByAthleteId(@PathVariable Long athleteId) {
        List<NutritionLog> logs = nutritionLogRepository.findByAthleteId(athleteId);
        if (logs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(logs);
    }
}
