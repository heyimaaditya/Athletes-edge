package com.athletesedge.service;

import com.athletesedge.dto.LogRecoveryRequest;
import com.athletesedge.model.RecoveryLog;
import com.athletesedge.repository.RecoveryLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class RecoveryService {

    @Autowired
    private RecoveryLogRepository repository;

    public RecoveryLog logOrUpdateRecovery(LogRecoveryRequest request) {
        LocalDate today = LocalDate.now();
        RecoveryLog log = repository.findByAthleteIdAndLogDate(request.getAthleteId(), today)
                .orElse(new RecoveryLog()); 

        log.setAthleteId(request.getAthleteId());
        log.setLogDate(today);
        log.setSleepQuality(request.getSleepQuality());
        log.setMuscleSoreness(request.getMuscleSoreness());
        log.setMood(request.getMood());
        log.setReadinessScore(calculateReadiness(request));

        return repository.save(log);
    }

    private int calculateReadiness(LogRecoveryRequest request) {
       
        double sleepScore = (request.getSleepQuality() - 1) * 25.0; // 1=0, 5=100
        double moodScore = (request.getMood() - 1) * 25.0; // 1=0, 5=100
        
      
        double sorenessScore = (5 - request.getMuscleSoreness()) * 25.0;

        double finalScore = (sleepScore * 0.5) + (moodScore * 0.3) + (sorenessScore * 0.2);

        return (int) Math.round(finalScore);
    }

    public Optional<RecoveryLog> getTodaysRecoveryLog(Long athleteId) {
        return repository.findByAthleteIdAndLogDate(athleteId, LocalDate.now());
    }
}