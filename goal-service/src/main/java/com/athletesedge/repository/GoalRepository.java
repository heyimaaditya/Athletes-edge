
package com.athletesedge.repository;

import com.athletesedge.model.Goal;
import com.athletesedge.model.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByAthleteIdAndStatus(Long athleteId, GoalStatus status);
}