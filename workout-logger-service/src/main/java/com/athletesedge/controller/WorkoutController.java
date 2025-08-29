package com.athletesedge.controller;

import com.athletesedge.model.Workout;
import com.athletesedge.service.WorkoutService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<Workout> createWorkoutLog(@RequestBody Workout workout) {
        Workout loggedWorkout = workoutService.logWorkout(workout);
        return new ResponseEntity<>(loggedWorkout, HttpStatus.CREATED);
    }

    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<Workout>> getWorkoutsByAthleteId(@PathVariable Long athleteId) {
        List<Workout> workouts = workoutService.getWorkoutsForAthlete(athleteId);
        return ResponseEntity.ok(workouts);
    }
}