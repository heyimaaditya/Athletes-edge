package com.athletesedge.repository;

import com.athletesedge.model.DailyWorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyWorkoutLogRepository extends JpaRepository<DailyWorkoutLog, Long> {
    List<DailyWorkoutLog> findByAthleteIdAndWorkoutDateBetween(Long athleteId, LocalDate startDate, LocalDate endDate);
}