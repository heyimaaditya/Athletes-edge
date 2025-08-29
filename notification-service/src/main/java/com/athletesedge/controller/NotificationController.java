package com.athletesedge.controller;

import com.athletesedge.service.NotificationSseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationSseService sseService;

    @GetMapping("/subscribe/{athleteId}")
    public SseEmitter subscribe(@PathVariable Long athleteId) {
        return sseService.addEmitter(athleteId);
    }
}