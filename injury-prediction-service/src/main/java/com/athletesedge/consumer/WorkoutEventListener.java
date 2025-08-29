package com.athletesedge.consumer;

import com.athletesedge.dto.WorkoutEventDTO;
import com.athletesedge.service.AcwrCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WorkoutEventListener {

    @Autowired
    private AcwrCalculationService acwrService;

    @KafkaListener(topics = "workout-events", groupId = "injury-prediction-group")
    public void listen(WorkoutEventDTO event) {
        System.out.println("Received workout event in Injury Prediction Service for athlete: " + event.getAthleteId());
        acwrService.processWorkout(event);
    }
}
