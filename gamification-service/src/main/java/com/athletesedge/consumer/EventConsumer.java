package com.athletesedge.consumer;

import com.athletesedge.dto.GoalCompletedEventDTO;
import com.athletesedge.dto.PrLoggedEventDTO;
import com.athletesedge.dto.WorkoutEventDTO;
import com.athletesedge.service.GamificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {
    @Autowired private GamificationService gamificationService;
    private static final int XP_PER_WORKOUT = 10;
    private static final int XP_PER_PR = 100;
    private static final int XP_PER_GOAL = 200;

    @KafkaListener(topics = "workout-events", containerFactory = "workoutKafkaListenerContainerFactory")
    public void consumeWorkoutEvent(WorkoutEventDTO event) {
        System.out.println("XP for workout: " + XP_PER_WORKOUT);
        gamificationService.addXp(event.getAthleteId(), XP_PER_WORKOUT);
    }
    
    @KafkaListener(topics = "pr-events", containerFactory = "prKafkaListenerContainerFactory")
    public void consumePrEvent(PrLoggedEventDTO event) {
        System.out.println("XP for PR: " + XP_PER_PR);
        gamificationService.addXp(event.getAthleteId(), XP_PER_PR);
    }
    
    @KafkaListener(topics = "goal-completed-events", containerFactory = "goalKafkaListenerContainerFactory")
    public void consumeGoalEvent(GoalCompletedEventDTO event) {
        System.out.println("XP for goal completion: " + XP_PER_GOAL);
        gamificationService.addXp(event.getAthleteId(), XP_PER_GOAL);
    }
}
