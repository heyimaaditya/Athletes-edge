package com.athletesedge.service;

import com.athletesedge.dto.CreateGoalRequest;
import com.athletesedge.model.*;
import com.athletesedge.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;

    public Goal createGoal(CreateGoalRequest request) {
        Goal goal = Goal.builder()
                .athleteId(request.getAthleteId())
                .description(request.getDescription())
                .type(request.getType())
                .targetValue(request.getTargetValue())
                .currentValue(0.0) 
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(GoalStatus.IN_PROGRESS)
                .build();
        return goalRepository.save(goal);
    }
    
    public List<Goal> getActiveGoalsForAthlete(Long athleteId) {
        return goalRepository.findByAthleteIdAndStatus(athleteId, GoalStatus.IN_PROGRESS);
    }
     public List<Goal> getGoalsForAthleteByStatus(Long athleteId, GoalStatus status) {
        return goalRepository.findByAthleteIdAndStatus(athleteId, status);
    }
}