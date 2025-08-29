package com.athletesedge.consumer;

import com.athletesedge.dto.GamificationUpdateDTO;
import com.athletesedge.service.NotificationSseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class GamificationUpdateListener {

    private static final Logger log = LoggerFactory.getLogger(GamificationUpdateListener.class);

    @Autowired
    private NotificationSseService sseService;

    @KafkaListener(
        topics = "gamification-updates",
        groupId = "notification-group",
        containerFactory = "gamificationUpdateKafkaListenerContainerFactory"
    )
    public void onUpdate(GamificationUpdateDTO update) {
        log.info("Gamification update for athlete {}", update.getAthleteId());
        sseService.sendEvent(update.getAthleteId(), "gamification-update", update);
    }
}

