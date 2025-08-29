package com.athletesedge.dto;
import lombok.Data;
import java.time.LocalDate;
@Data public class WorkoutDto { private LocalDate workoutDate; private String type; private Integer durationMinutes; }