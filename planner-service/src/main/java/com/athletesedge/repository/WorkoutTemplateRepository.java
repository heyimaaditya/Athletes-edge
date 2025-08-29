package com.athletesedge.repository;

import com.athletesedge.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Long> {
}