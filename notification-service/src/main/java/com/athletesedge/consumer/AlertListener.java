package com.athletesedge.consumer;

import com.athletesedge.dto.InjuryRiskAlertDTO;
import com.athletesedge.service.NotificationSseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AlertListener {

    private static final Logger logger = LoggerFactory.getLogger(AlertListener.class);
    
    @Autowired 
    private NotificationSseService sseService;

    @KafkaListener(topics = "injury-risk-alert", groupId = "notification-group",containerFactory = "injuryAlertKafkaListenerContainerFactory")
    public void listenForAlerts(InjuryRiskAlertDTO alert) {
        logger.info("<<<<< Kafka Alert Received for Athlete ID: {} >>>>>", alert.getAthleteId());
        
        
        sseService.sendNotification(alert.getAthleteId(), alert);
    }
}
