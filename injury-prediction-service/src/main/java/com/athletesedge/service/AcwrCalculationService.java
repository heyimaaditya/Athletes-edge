package com.athletesedge.service;

import com.athletesedge.dto.InjuryRiskAlertDTO;
import com.athletesedge.dto.WorkoutEventDTO;
import com.athletesedge.model.DailyWorkoutLog;
import com.athletesedge.repository.DailyWorkoutLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import com.athletesedge.dto.AcwrDataPointDTO; 
import java.util.ArrayList; 
import java.util.Collections;
import java.util.stream.Collectors; 
import java.util.Map; 

@Service
public class AcwrCalculationService {

    @Autowired
    private DailyWorkoutLogRepository logRepository;

    @Autowired
    private KafkaTemplate<String, InjuryRiskAlertDTO> kafkaTemplate;

    private static final String ALERT_TOPIC = "injury-risk-alert";
    private static final double DANGER_ZONE_THRESHOLD = 1.5;

    public void processWorkout(WorkoutEventDTO event) {
        
        saveWorkoutLog(event);

   
        LocalDate today = event.getWorkoutDate();
        Long athleteId = event.getAthleteId();

        double acuteLoad = calculateLoadForPeriod(athleteId, today.minusDays(6), today);
        double chronicLoad = calculateLoadForPeriod(athleteId, today.minusDays(27), today) / 4.0; 

        if (chronicLoad < 1) {
            System.out.println("Chronic load is too low to calculate ACWR for athlete " + athleteId);
            return;
        }

        double acwr = acuteLoad / chronicLoad;
        System.out.printf("Athlete %d - Acute: %.2f, Chronic: %.2f, ACWR: %.2f%n", athleteId, acuteLoad, chronicLoad, acwr);

      
        if (acwr > DANGER_ZONE_THRESHOLD) {
            sendAlert(athleteId, acwr);
        }
    }

    private void saveWorkoutLog(WorkoutEventDTO event) {
        DailyWorkoutLog log = DailyWorkoutLog.builder()
                .athleteId(event.getAthleteId())
                .workoutDate(event.getWorkoutDate())
                .trainingLoad(event.getTrainingLoad())
                .build();
        logRepository.save(log);
    }

    private double calculateLoadForPeriod(Long athleteId, LocalDate startDate, LocalDate endDate) {
        List<DailyWorkoutLog> logs = logRepository.findByAthleteIdAndWorkoutDateBetween(athleteId, startDate, endDate);
        return logs.stream().mapToDouble(DailyWorkoutLog::getTrainingLoad).sum();
    }

    private void sendAlert(Long athleteId, double acwr) {
        String message = String.format("High injury risk detected! ACWR is %.2f, which is above the threshold of %.2f.", acwr, DANGER_ZONE_THRESHOLD);
        InjuryRiskAlertDTO alert = new InjuryRiskAlertDTO(athleteId, acwr, message, "HIGH");
        
        kafkaTemplate.send(ALERT_TOPIC, String.valueOf(athleteId), alert);
        System.out.println("!!! ALERT SENT for athlete " + athleteId + " !!!");
    }

    public List<AcwrDataPointDTO> getAcwrHistory(Long athleteId, int days) {
        List<AcwrDataPointDTO> history = new ArrayList<>();
        LocalDate today = LocalDate.now();

        
        List<DailyWorkoutLog> allLogs = logRepository.findByAthleteIdAndWorkoutDateBetween(
            athleteId, today.minusDays(days + 28), today
        );
        
       
        Map<LocalDate, Double> dailyLoadMap = allLogs.stream()
            .collect(Collectors.groupingBy(DailyWorkoutLog::getWorkoutDate, Collectors.summingDouble(DailyWorkoutLog::getTrainingLoad)));

        for (int i = 0; i < days; i++) {
            LocalDate currentDate = today.minusDays(i);
            
            double acuteLoad = 0;
            for (int j = 0; j < 7; j++) {
                acuteLoad += dailyLoadMap.getOrDefault(currentDate.minusDays(j), 0.0);
            }

            double chronicLoad = 0;
            for (int j = 0; j < 28; j++) {
                chronicLoad += dailyLoadMap.getOrDefault(currentDate.minusDays(j), 0.0);
            }
            chronicLoad /= 4.0; 

            if (chronicLoad > 1) {
                double acwr = acuteLoad / chronicLoad;
                history.add(new AcwrDataPointDTO(currentDate, acwr));
            }
        }
      
        Collections.reverse(history);
        return history;
    }
}
