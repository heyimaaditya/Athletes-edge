package com.athletesedge.controller;

import com.athletesedge.dto.TimelineEventDto;
import com.athletesedge.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/timeline")
public class TimelineController {

    @Autowired
    private TimelineService timelineService;

    @GetMapping("/athlete/{athleteId}")
    public Mono<List<TimelineEventDto>> getTimeline(
            @PathVariable Long athleteId,
            @RequestHeader("Authorization") String authToken) {
        return timelineService.getTimelineForAthlete(athleteId, authToken);
    }
}
