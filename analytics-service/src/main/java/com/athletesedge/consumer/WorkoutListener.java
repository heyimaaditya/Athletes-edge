package com.athletesedge.consumer;

import com.athletesedge.dto.WorkoutEventDTO;
import com.athletesedge.model.AthleteStats;
import com.athletesedge.repository.AthleteStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WorkoutListener {

    @Autowired
    private AthleteStatsRepository athleteStatsRepository;


    @KafkaListener(topics = "workout-events", groupId = "analytics-group")
    public void consumeWorkoutEvent(WorkoutEventDTO event) {
        System.out.println("Received workout event for athlete ID: " + event.getAthleteId());


        AthleteStats stats = athleteStatsRepository.findById(event.getAthleteId())

                .orElse(AthleteStats.builder()
                        .athleteId(event.getAthleteId())
                        .workoutCount(0)
                        .totalTrainingLoad(0.0)
                        .build());

        stats.setTotalTrainingLoad(stats.getTotalTrainingLoad() + event.getTrainingLoad());
        stats.setWorkoutCount(stats.getWorkoutCount() + 1);
        stats.setLastWorkoutDate(event.getWorkoutDate());

        athleteStatsRepository.save(stats);

        System.out.println("Updated stats for athlete ID " + event.getAthleteId() + ": " + stats);
    }
}