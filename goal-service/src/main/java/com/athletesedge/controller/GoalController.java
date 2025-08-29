package com.athletesedge.controller;

import com.athletesedge.dto.CreateGoalRequest;
import com.athletesedge.model.Goal;
import com.athletesedge.model.GoalStatus;
import com.athletesedge.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody CreateGoalRequest request) {
        Goal newGoal = goalService.createGoal(request);
        return new ResponseEntity<>(newGoal, HttpStatus.CREATED);
    }

   
    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<Goal>> getGoalsByAthleteIdAndStatus(
            @PathVariable Long athleteId,
            @RequestParam(required = false, defaultValue = "IN_PROGRESS") String status) {
        
        try {
           
            GoalStatus goalStatus = GoalStatus.valueOf(status.toUpperCase());
            List<Goal> goals = goalService.getGoalsForAthleteByStatus(athleteId, goalStatus);
            return ResponseEntity.ok(goals);
        } catch (IllegalArgumentException e) {
          
            return ResponseEntity.badRequest().build();
        }
    }
}
