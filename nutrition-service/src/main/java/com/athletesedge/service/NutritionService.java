package com.athletesedge.service;

import com.athletesedge.dto.NutritionEventDTO;
import com.athletesedge.model.NutritionLog;
import com.athletesedge.repository.NutritionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NutritionService {

    @Autowired
    private NutritionLogRepository nutritionLogRepository;

    @Autowired
    private KafkaTemplate<String, NutritionEventDTO> kafkaTemplate;

    private static final String KAFKA_TOPIC = "nutrition-events";

    public NutritionLog createLog(NutritionLog nutritionLog) {
        if (nutritionLog.getLogDate() == null) {
            nutritionLog.setLogDate(LocalDate.now());
        }
        NutritionLog savedLog = nutritionLogRepository.save(nutritionLog);

    
        NutritionEventDTO event = new NutritionEventDTO(
            savedLog.getAthleteId(),
            savedLog.getCalories(),
            savedLog.getProteinGrams(),
            savedLog.getCarbsGrams(),
            savedLog.getFatGrams(),
            savedLog.getLogDate()
        );
        kafkaTemplate.send(KAFKA_TOPIC, String.valueOf(savedLog.getAthleteId()), event);
        System.out.println("Nutrition event sent to Kafka: " + event);

        return savedLog;
    }
}
