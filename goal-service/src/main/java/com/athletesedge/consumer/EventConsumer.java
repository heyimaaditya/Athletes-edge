package com.athletesedge.consumer;
import com.athletesedge.dto.GoalCompletedEventDTO;

import com.athletesedge.dto.NutritionEventDTO;
import com.athletesedge.dto.WorkoutEventDTO;
import com.athletesedge.model.*;
import com.athletesedge.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class EventConsumer {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private KafkaTemplate<String, GoalCompletedEventDTO> kafkaTemplate; 

    private static final String KAFKA_TOPIC = "goal-completed-events";
  
    @KafkaListener(topics = "workout-events", containerFactory = "workoutKafkaListenerContainerFactory")
    public void consumeWorkoutEvent(WorkoutEventDTO event) {
        System.out.println("Goal service consumed workout event: " + event);
        List<Goal> activeGoals = goalRepository.findByAthleteIdAndStatus(event.getAthleteId(), GoalStatus.IN_PROGRESS);

        for (Goal goal : activeGoals) {
            boolean updated = false;
            if (goal.getType() == GoalType.WORKOUT_COUNT) {
                goal.setCurrentValue(goal.getCurrentValue() + 1);
                updated = true;
            } else if (goal.getType() == GoalType.TOTAL_TRAINING_LOAD) {
                goal.setCurrentValue(goal.getCurrentValue() + event.getTrainingLoad());
                updated = true;
            }
            
            if (updated) {
                checkAndUpdateGoalStatus(goal);
                goalRepository.save(goal);
            }
        }
    }

  
    @KafkaListener(topics = "nutrition-events", containerFactory = "nutritionKafkaListenerContainerFactory")
    public void consumeNutritionEvent(NutritionEventDTO event) {
        System.out.println("Goal service consumed nutrition event: " + event);
        List<Goal> activeGoals = goalRepository.findByAthleteIdAndStatus(event.getAthleteId(), GoalStatus.IN_PROGRESS);

        for (Goal goal : activeGoals) {
            boolean updated = false;
            if (goal.getType() == GoalType.NUTRITION_CALORIES) {
                goal.setCurrentValue(goal.getCurrentValue() + event.getCalories());
                updated = true;
            } else if (goal.getType() == GoalType.NUTRITION_PROTEIN) {
                goal.setCurrentValue(goal.getCurrentValue() + event.getProteinGrams());
                updated = true;
            }

            if (updated) {
                checkAndUpdateGoalStatus(goal);
                goalRepository.save(goal);
            }
        }
    }

    private void checkAndUpdateGoalStatus(Goal goal) {
        if (goal.getCurrentValue() >= goal.getTargetValue()) {
            goal.setStatus(GoalStatus.COMPLETED);
             GoalCompletedEventDTO event = new GoalCompletedEventDTO(goal.getAthleteId(), goal.getDescription());
            kafkaTemplate.send(KAFKA_TOPIC, String.valueOf(goal.getAthleteId()), event);
            System.out.println("Goal Completed event sent to Kafka: " + event);
        }
    }
}