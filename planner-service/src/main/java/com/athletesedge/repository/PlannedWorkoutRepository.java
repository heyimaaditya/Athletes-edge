package com.athletesedge.repository;

import com.athletesedge.model.PlannedWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PlannedWorkoutRepository extends JpaRepository<PlannedWorkout, Long> {
    List<PlannedWorkout> findByAthleteIdAndPlannedDateBetween(Long athleteId, LocalDate start, LocalDate end);
    List<PlannedWorkout> findByAthleteIdAndPlannedDate(Long athleteId, LocalDate date);
}