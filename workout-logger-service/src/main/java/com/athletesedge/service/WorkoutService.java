package com.athletesedge.service;

import com.athletesedge.dto.WorkoutEventDTO;
import com.athletesedge.model.Workout;
import com.athletesedge.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private KafkaTemplate<String, WorkoutEventDTO> kafkaTemplate;

    private static final String KAFKA_TOPIC = "workout-events";

    public Workout logWorkout(Workout workout) {
       
        if (workout.getWorkoutDate() == null) {
            workout.setWorkoutDate(LocalDate.now());
        }

      
        Workout savedWorkout = workoutRepository.save(workout);

       
        double trainingLoad = savedWorkout.getDurationMinutes() * savedWorkout.getIntensity();

       
        WorkoutEventDTO event = new WorkoutEventDTO(
                savedWorkout.getAthleteId(),
                savedWorkout.getType(),
                savedWorkout.getDurationMinutes(),
                savedWorkout.getIntensity(),
                savedWorkout.getWorkoutDate(),
                trainingLoad
        );

        
        kafkaTemplate.send(KAFKA_TOPIC, String.valueOf(event.getAthleteId()), event);
        
        System.out.println("Workout event sent to Kafka: " + event);

        return savedWorkout;
    }

    public List<Workout> getWorkoutsForAthlete(Long athleteId) {
        return workoutRepository.findByAthleteIdOrderByWorkoutDateDesc(athleteId);
    }
}
