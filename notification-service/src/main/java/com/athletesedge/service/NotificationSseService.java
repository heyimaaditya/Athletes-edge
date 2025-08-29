package com.athletesedge.service;

import com.athletesedge.dto.InjuryRiskAlertDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationSseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(Long athleteId) {
       
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

       
        this.emitters.put(athleteId, emitter);

      
        emitter.onCompletion(() -> this.emitters.remove(athleteId));
       
        emitter.onTimeout(() -> this.emitters.remove(athleteId));

        return emitter;
    }

     public void sendNotification(Long athleteId, InjuryRiskAlertDTO alert) {
        sendEvent(athleteId, "injury-alert", alert);
    }

    public void sendEvent(Long athleteId, String eventName, Object payload) {
        SseEmitter emitter = this.emitters.get(athleteId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(payload));
            } catch (IOException e) {
                this.emitters.remove(athleteId);
            }
        }
    }
}
