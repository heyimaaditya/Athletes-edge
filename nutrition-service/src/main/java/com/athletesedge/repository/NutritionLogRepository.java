package com.athletesedge.repository;

import com.athletesedge.model.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {

   
    List<NutritionLog> findByAthleteId(Long athleteId);
}
